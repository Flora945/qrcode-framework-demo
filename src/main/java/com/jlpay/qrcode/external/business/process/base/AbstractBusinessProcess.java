package com.jlpay.qrcode.external.business.process.base;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.commons.events.EventLevel;
import com.jlpay.qrcode.external.commons.events.EventReporter;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionMessages;
import com.jlpay.qrcode.external.commons.exceptions.assertion.Assert;
import com.jlpay.qrcode.external.commons.exceptions.def.BaseException;
import com.jlpay.qrcode.external.commons.exceptions.def.BusiException;
import com.jlpay.qrcode.external.commons.exceptions.def.SystemException;
import com.jlpay.qrcode.external.commons.io.client.ReactiveHttpTool;
import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import com.jlpay.qrcode.external.controller.BusiProcessContext;
import com.jlpay.qrcode.external.controller.BusinessProcessor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * 业务处理流程
 *
 * @param <R> 请求对象类型
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractBusinessProcess<R> implements BusinessProcessor {

    private EventReporter eventReporter;

    private SmartValidator validator;

    private ReactiveHttpTool httpTool;

    private Executor executor;

    @Value("${config.business.default-invoke-timeout:34s}")
    private Duration defaultInvokeTimeout;

    private R request;

    protected BusiProcessContext processContext;

    /**
     * handleBusiness
     */
    protected abstract void handleBusiness();

    protected R getRequest() {
        if (request == null) {
            request = parseRequest();
        }
        return request;
    }

    protected void completeResponse(Object response) {
        String strResp = response instanceof String ? (String) response : JSON.toJSONString(response);
        processContext.writeResponse(strResp);
        processContext.completeProcess();
    }

    @Override
    public void process(BusiProcessContext processContext) {
        this.processContext = processContext;
        try {
            handleBusiness();
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * 调用下级服务
     *
     * @param serviceResponseType 响应类型class
     * @param <SR>                响应类型
     * @return 调用规范对象
     */
    protected <SR> InvokeSpec<SR> invokeFor(Class<SR> serviceResponseType) {
        return new InvokeSpec<>(serviceResponseType);
    }

    protected void handleException(Throwable e) {
        String retCode = e instanceof BaseException ? ((BaseException) e).getCode() : ExceptionCodes.SYSTEM_EXCEPTION;
        completeResponse(DefaultResponse.fromCodeAndMsg(retCode, e.getMessage()));
        if (e instanceof SystemException) {
            log.error("ERROR", e);
            sendEventReport(e);
        }
    }

    protected void sendEventReport(Throwable e) {
        eventReporter.prepareReport()
                .level(EventLevel.FATAL)
                .cause(e)
                .send();
    }

    @SuppressWarnings("unchecked")
    protected R parseRequest() {
        ServiceConfig serviceConfigAnno = getClass().getAnnotation(ServiceConfig.class);
        Assert.isTrue(serviceConfigAnno != null, ExceptionCodes.SYSTEM_EXCEPTION, ExceptionMessages.SYSTEM_EXCEPTION_MESSAGE, "没有配置ServiceConfig");
        Class<R> requestType = (Class<R>) serviceConfigAnno.requestType();
        return processContext.getParsedRequest().toJavaObject(requestType);
    }

    /**
     * 发起服务调用
     *
     * @param invokeSpec 调用规范对象
     * @param <SR> 调用结果类型
     */
    private <SR> void invokeService(InvokeSpec<SR> invokeSpec) {
        validate(invokeSpec);
        httpTool.preparePost()
                .url(invokeSpec.getServicePath())
                .logId(processContext.getLogId())
                .contentType(MediaType.APPLICATION_JSON)
                .requestTimeout(invokeSpec.getRequestTimeout())
                .requestBody(invokeSpec.getRequestBody())
                .perform()
                .map(resp -> JSON.parseObject(resp, invokeSpec.getResponseType()))
                .doOnError(e -> executor.execute(() -> invokeSpec.errorHandler.accept(e)))
                .subscribe(response -> executor.execute(() -> {
                    Assert.isTrue(!processContext.completed(), ExceptionCodes.SYSTEM_EXCEPTION, ExceptionMessages.SYSTEM_EXCEPTION_MESSAGE,
                            "会话已经超时，不再处理业务，需要代码使用者维护会话超时时间，会话超时时间不能小于请求建立链接和应答处理时常");
                    try {
                        invokeSpec.responseHandler.accept(response);
                    } catch (Exception e) {
                        invokeSpec.errorHandler.accept(e);
                    }
                }));
    }

    /**
     * 校验参数
     */
    private void validate(InvokeSpec<?> spec) {
        DirectFieldBindingResult bindingResult = new DirectFieldBindingResult(spec, "invokeChainSpec");
        validator.validate(spec, bindingResult);
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            String msg = String.format("%s, key: %s, value: %s", fieldError.getDefaultMessage(), fieldError.getField(), fieldError.getRejectedValue());
            throw new BusiException(ExceptionCodes.BAD_REQUEST_PARAM, msg);
        }
    }

    public BusiProcessContext getProcessContext() {
        return processContext;
    }

    @Autowired
    public void setExecutor(@Qualifier("business") Executor executor) {
        this.executor = executor;
    }

    @Autowired
    public void setEventReporter(EventReporter eventReporter) {
        this.eventReporter = eventReporter;
    }

    @Autowired
    public void setValidator(SmartValidator validator) {
        this.validator = validator;
    }

    @Autowired
    public void setHttpTool(ReactiveHttpTool httpTool) {
        this.httpTool = httpTool;
    }

    /**
     * 调用规范对象，用于组建一次调用
     * @param <SR> 调用结果对象类型
     */
    @RequiredArgsConstructor
    @Getter(value = AccessLevel.PRIVATE)
    public class InvokeSpec<SR> {

        /**
         * 调用服务地址
         */
        @NotBlank
        private String servicePath;

        /**
         * 请求超时时间
         */
        @NotNull
        private Duration requestTimeout = defaultInvokeTimeout;

        /**
         * 请求内容
         */
        private Object requestBody;

        /**
         * 响应处理器
         */
        @NotNull
        private Consumer<SR> responseHandler;

        /**
         * 异常处理器
         */
        @NotNull
        private Consumer<Throwable> errorHandler = AbstractBusinessProcess.this::handleException;

        /**
         * 响应类型
         */
        private final Class<SR> responseType;

        /**
         * 发起调用
         */
        public void invoke() {
            invokeService(this);
        }

        public InvokeSpec<SR> servicePath(String servicePath) {
            this.servicePath = servicePath;
            return this;
        }

        public InvokeSpec<SR> requestBody(Object request) {
            this.requestBody = request;
            return this;
        }

        public InvokeSpec<SR> onResponse(Consumer<SR> responseHandler) {
            this.responseHandler = responseHandler;
            return this;
        }

        public InvokeSpec<SR> errorHandler(Consumer<Throwable> errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        public InvokeSpec<SR> requestTimeout(Duration requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }
    }

}







