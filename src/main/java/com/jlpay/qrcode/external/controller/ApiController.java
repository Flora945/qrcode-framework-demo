package com.jlpay.qrcode.external.controller;

import brave.Tracing;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jlpay.qrcode.external.commons.Constants;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionMessages;
import com.jlpay.qrcode.external.commons.exceptions.assertion.Assert;
import com.jlpay.qrcode.external.commons.exceptions.def.BusiException;
import com.jlpay.qrcode.external.commons.exceptions.def.SystemException;
import com.jlpay.qrcode.external.commons.io.IoUtil;
import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.SpanNamer;
import org.springframework.cloud.sleuth.instrument.async.TraceRunnable;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

/**
 * HTTP请求入口
 *
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Slf4j
@Setter
@RestController
@RequiredArgsConstructor
public class ApiController {

    private static final String ASYNC_SUPPORTED = "org.apache.catalina.ASYNC_SUPPORTED";

    private final Tracing tracing;

    private final SpanNamer defaultSpanNamer;

    private final ApplicationContext applicationContext;

    @Value("${server.tomcat.async-conext.timeout}")
    private long asyncContextTimeout;

    private Executor executor;

    @PostMapping({"/qrcode/trans/gateway/", "/api/pay/"})
    public void qrcodeTransGateway(HttpServletRequest request, HttpServletResponse response) {
        HttpContext httpContext = createProcessContext(request, response, null);
        processBusi(httpContext);
    }

    @PostMapping(value = "/qrcode/trans/gateway/{commandId}")
    public void qrcodeTransGateway(HttpServletRequest request, HttpServletResponse response, @PathVariable String commandId) {
        String tempCommandId = commandId.contains(ApiConstants.SERVICE_PREFIX) ? commandId : ApiConstants.SERVICE_PREFIX + commandId;
        HttpContext httpContext = createProcessContext(request, response, tempCommandId);
        processBusi(httpContext);
    }

    @PostMapping(value = "/api/pay/{commandId}")
    public void api(HttpServletRequest request, HttpServletResponse response, @PathVariable String commandId) {
        if ("micropay".equals(commandId)) {
            commandId = "micropayasyn";
        }
        qrcodeTransGateway(request, response, commandId);
    }

    /**
     * 接收业务请求
     *
     * @param uri
     * @param request
     * @param response
     */
    @RequestMapping("/{uri}")
    public void server(@PathVariable String uri, HttpServletRequest request, HttpServletResponse response) {
        HttpContext httpContext = createProcessContext(request, response, uri);
        processBusi(httpContext);
    }

    @GetMapping(value = "/qrcode/trans/status")
    public String index() {
        log.info("心跳检测");
        return "JLPAY EXT QRCODE";
    }

    private void processBusi(HttpContext httpContext) {
        executor.execute(new TraceRunnable(tracing, defaultSpanNamer, () -> {
            log.info("收到请求报文为:{}", httpContext.getRawRequest());
            String requestName = httpContext.getRequestName();
            try {
                BusinessProcessor businessProcessor = applicationContext.getBean(requestName, BusinessProcessor.class);
                businessProcessor.process(httpContext);
            } catch (NoSuchBeanDefinitionException e) {
                responseFromBusiException(httpContext, new BusiException(ExceptionCodes.BAD_REQUEST_PARAM, "找不到处理对象: " + requestName));
            } catch (BusiException e) {
                responseFromBusiException(httpContext, e);
            } catch (Exception e) {
                log.error("ERROR", e);
                responseFromException(httpContext, e);
            }
        }));
    }

    private void responseFromBusiException(HttpContext context, BusiException e) {
        DefaultResponse resp = new DefaultResponse();
        resp.setRetCode(e.getCode());
        resp.setRetMsg(e.getMessage());
        context.writeResponse(JSON.toJSONString(resp));
        context.completeProcess();
    }

    private void responseFromException(HttpContext httpContext, Exception e) {
        AsyncContext asyncContext = httpContext.getAsyncContext();
        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
        response.setStatus(500);
        httpContext.writeResponse(e.getMessage());
    }

    /**
     * 创建HttpContext
     *
     * @param request   servletRequest
     * @param response  servletResponse
     * @param commandId commandId如果为null的话则从报文中解析
     * @return httpContext
     */
    private HttpContext createProcessContext(HttpServletRequest request, HttpServletResponse response, String commandId) {
        request.setAttribute(ASYNC_SUPPORTED, true);
        String logId = MDC.get(Constants.MDC_LOG_ID_KEY);
        AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(asyncContextTimeout);

        HttpContext httpContext = new HttpContext(asyncContext, logId);

        JSONObject reqJson = httpContext.getParsedRequest();
        String requestName = StringUtils.isNotBlank(commandId) ? commandId : reqJson.getString("service");
        log.info("请求URL: {}, 机构号: {}, 接口名: {}", request.getRequestURL().toString(), reqJson.getString("org_code"), requestName);
        httpContext.setRequestName(requestName);
        return httpContext;
    }


    @Autowired
    public void setExecutor(@Qualifier("business") Executor executor) {
        this.executor = executor;
    }

    @Setter
    private static class HttpContext extends AbstractBusiProcessContext {

        private final AsyncContext asyncContext;

        private HttpContext(AsyncContext asyncContext, String logId) {
            setLogId(logId);
            this.asyncContext = asyncContext;
        }

        public AsyncContext getAsyncContext() {
            return asyncContext;
        }

        @Override
        public String getRawRequest() {
            if (rawRequest == null) {
                rawRequest = new String(IoUtil.getRequestBody(asyncContext.getRequest()), StandardCharsets.UTF_8);
            }
            return rawRequest;
        }

        @Override
        public void writeResponse(String response) {
            // maybe compare and swap is better
            Assert.isTrue(!completed, ExceptionCodes.SYSTEM_EXCEPTION, ExceptionMessages.SYSTEM_EXCEPTION_MESSAGE, "会话已结束");
            completed = true;
            long cost = System.currentTimeMillis() - startTimestamp;
            log.info("请求[{}]耗时: {}ms, 写入响应: {}", requestName, cost, response);
            IoUtil.writeResponse(response.getBytes(StandardCharsets.UTF_8), asyncContext.getResponse());
        }

        @Override
        public void completeProcess() {
            asyncContext.complete();
            super.completeProcess();
        }

    }
}
