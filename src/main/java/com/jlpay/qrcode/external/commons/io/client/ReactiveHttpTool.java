package com.jlpay.qrcode.external.commons.io.client;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.commons.Constants;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;
import com.jlpay.qrcode.external.commons.exceptions.assertion.BusiAssert;
import com.jlpay.qrcode.external.commons.exceptions.def.ConnectException;
import com.jlpay.qrcode.external.commons.exceptions.def.HttpStatusNotOkException;
import com.jlpay.qrcode.external.commons.exceptions.def.ReadTimeoutException;
import com.jlpay.qrcode.external.commons.exceptions.def.SystemException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Http请求工具类
 *
 * @author qihuaiyuan
 * @since 2021/02/05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReactiveHttpTool {

    private final HttpClientProperties properties;

    private WebClient webClient;

    @PostConstruct
    public void configureHttpClient() {
        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(opt ->
                // 设置连接超时时间
                opt.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeoutMillis())
                        // 设置请求超时时间
                        .afterNettyContextInit(ctx -> ctx.addHandlerLast(new ReadTimeoutHandler(properties.getMaxRequestTimeout().toMillis(), TimeUnit.MILLISECONDS)))
        );
        webClient = WebClient.builder()
                .clientConnector(httpConnector)
                .build();
    }

    /**
     * 准备发送GET请求
     *
     * @return 用于组装HTTP请求的对象
     */
    public HttpRequestSpec prepareGet() {
        return new HttpRequestSpec().method(HttpMethod.GET);
    }

    /**
     * 准备发送POST请求
     *
     * @return 用于组装HTTP请求的对象
     */
    public HttpRequestSpec preparePost() {
        return new HttpRequestSpec().method(HttpMethod.POST);
    }

    /**
     * 用于组装HTTP请求的对象
     */
    public class HttpRequestSpec {

        /**
         * 请求METHOD
         */
        private HttpMethod method;

        /**
         * 请求路径
         */
        private String requestPath;

        /**
         * 请求query parameter
         */
        private Map<String, Object[]> queryParams;

        /**
         * 请求头中的logId
         */
        private String logId;

        /**
         * 请求头Content-Type
         */
        private MediaType contentType = MediaType.APPLICATION_JSON;

        /**
         * 请求超时时间
         */
        private Duration requestTimeout = properties.getMaxRequestTimeout();

        /**
         * 请求体
         */
        private Object requestBody;

        /**
         * 执行HTTP请求
         *
         * @return 响应对象Mono
         */
        public Mono<String> perform() {
            // 设置HTTP METHOD
            WebClient.RequestBodyUriSpec uriSpec = webClient.method(this.method);
            // 设置请求URI
            BusiAssert.notBlank(requestPath, ExceptionCodes.BAD_REQUEST_PARAM, "请求地址为空");
            WebClient.RequestBodySpec bodySpec = uriSpec.uri(requestPath)
                    .contentType(contentType)
                    .header(Constants.MDC_LOG_ID_KEY, resolveLogId());
            log.info("发送HTTP {}, 地址: {}", method.name(), requestPath);
            // 设置请求体
            if (requestBody != null) {
                // 字符串形式的请求体
                String bodyValue = requestBody instanceof String ? (String) requestBody : JSON.toJSONString(requestBody);
                bodySpec.syncBody(bodyValue);
                log.info("请求体: {}", bodyValue);
            }
            // 执行HTTP请求
            long start = System.currentTimeMillis();
            return bodySpec
                    .retrieve()
                    .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                            clientResponse -> {
                                int status = clientResponse.statusCode().value();
                                String msg = MessageFormatter.format("请求地址: {} 返回httpStatus: {}", requestPath, status).getMessage();
                                return Mono.error(new HttpStatusNotOkException(status, "渠道异常", msg));
                            })
                    // 请求体转换为String
                    .bodyToMono(String.class)
                    // 设置超时时间
                    .timeout(requestTimeout)
                    // 打印耗时及响应内容
                    .doOnNext(strRsp -> {
                        long cost = System.currentTimeMillis() - start;
                        log.info("响应地址: {}, 耗时: {}ms, 内容: {}", requestPath, cost, strRsp);
                    })
                    // 处理异常，将其他异常转换为系统异常（自定义异常）
                    .onErrorMap(Exception.class, this::transformException);
        }

        /**
         * 转换为系统异常, 根据不同的异常类型转换为不同的自定义异常
         *
         * @param oriException 原始异常
         * @return 原始异常转换为系统异常，添加响应码响应信息及告警信息
         */
        private SystemException transformException(Exception oriException) {
            String warningMessage = formatWarningMessage(requestPath, oriException);
            // 连接超时
            if (oriException instanceof java.net.ConnectException) {
                return new ConnectException("连接超时", warningMessage, oriException);
            }
            // 读取超时
            // WebClient#requestTimeout -> java.concurrent.TimeoutException
            // Netty RequestTimeoutHandler -> io.netty.handler.timeout.ReadTimeoutException
            if (oriException instanceof TimeoutException
                    || oriException instanceof io.netty.handler.timeout.ReadTimeoutException) {
                return new ReadTimeoutException("读取超时", warningMessage, oriException);
            }
            return SystemException.builder()
                    .cause(oriException)
                    .exceptionCode(ExceptionCodes.SYSTEM_EXCEPTION)
                    .eventMessage(warningMessage)
                    .build();
        }

        private String formatWarningMessage(String requestPath, Throwable cause) {
            return MessageFormat.format("请求[{0}]异常：原因: {1}, 信息: {2}", requestPath, cause.getClass().getName(), cause.getMessage());
        }

        private String resolveLogId() {
            if (StringUtils.isNotBlank(logId)) {
                return logId;
            }
            String mdcLogId = MDC.get(Constants.MDC_LOG_ID_KEY);
            if (StringUtils.isNotBlank(mdcLogId)) {
                return mdcLogId;
            }
            return UUID.randomUUID().toString().replaceAll("-", "");
        }

        public HttpRequestSpec method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public HttpRequestSpec url(String requestPath) {
            this.requestPath = requestPath;
            return this;
        }

        public HttpRequestSpec queryParam(String key, Object... queryParam) {
            if (queryParams == null) {
                queryParams = new HashMap<>(10);
            }
            queryParams.put(key, queryParam);
            return this;
        }

        public HttpRequestSpec queryParams(Map<String, Object[]> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public HttpRequestSpec requestTimeout(long requestTimeoutMillis) {
            this.requestTimeout = Duration.ofMillis(requestTimeoutMillis);
            return this;
        }

        public HttpRequestSpec requestTimeout(Duration requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }

        public HttpRequestSpec requestBody(Object requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        /**
         * default: application/json
         *
         * @param contentType
         * @return
         */
        public HttpRequestSpec contentType(MediaType contentType) {
            this.contentType = contentType;
            return this;
        }

        public HttpRequestSpec logId(String logId) {
            this.logId = logId;
            return this;
        }

    }
}
