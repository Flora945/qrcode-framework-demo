package com.jlpay.qrcode.external.commons.events;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.commons.Constants;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;
import com.jlpay.qrcode.external.commons.exceptions.def.BaseException;
import com.jlpay.qrcode.external.commons.exceptions.def.SystemException;
import com.jlpay.qrcode.external.commons.io.client.ReactiveHttpTool;
import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import com.jlpay.qrcode.external.commons.io.protocol.Response;
import com.jlpay.qrcode.external.commons.util.NetworkUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventReporter {

    /**
     * 重复响应
     */
    private static final DefaultResponse RESPONSE_FOR_DUPLICATED =
            DefaultResponse.fromCodeAndMsg(Constants.RET_CODE_SUCCESS, Constants.DUPLICATED_WARNING_MESSAGE);

    private final EventReportProperties properties;

    private final ReactiveHttpTool httpTool;

    private PassiveExpiringMap<String, Object> eventReportRecord;

    public void postConstruct() {
        eventReportRecord = new PassiveExpiringMap<>(properties.getDropDuplicatesIn().toMillis(), new ConcurrentHashMap<>(15));
    }

    public EventReportSpec prepareReport() {
        return new EventReportSpec();
    }

    private Mono<? extends Response> sendReport(EventReportSpec reportSpec) {
        String keyword = reportSpec.getKeyword();
        if (properties.isDropDuplicates() && eventReportRecord.containsKey(keyword)) {
            if (log.isDebugEnabled()) {
                log.debug("重复告警: {}", keyword);
            }
            return Mono.just(RESPONSE_FOR_DUPLICATED);
        }
        // build request
        EventReportRequest request = buildRequest(reportSpec);
        // send request
        return httpTool.preparePost()
                .logId(request.getLogId())
                .contentType(MediaType.APPLICATION_JSON)
                .requestBody(request)
                .requestTimeout(properties.getRequestTimeout())
                .perform()
                .map(resp -> JSON.parseObject(resp, DefaultResponse.class))
                .doOnNext(resp -> record(keyword, resp));

    }

    /**
     * 记录告警以防重
     *
     * @param keyword  关键字
     * @param response 响应
     */
    private void record(String keyword, DefaultResponse response) {
        // 只有告警成功了才记录
        if (response.isSuccess()) {
            eventReportRecord.put(keyword, keyword);
        }
    }

    private EventReportRequest buildRequest(EventReportSpec reportSpec) {
        return EventReportRequest.builder()
                .model(properties.getModel())
                .service(properties.getServiceName())
                .ip(NetworkUtil.getLocalHostIp())
                .time(new Date())
                .level(reportSpec.level.getValue())
                .exeInterface(reportSpec.requestName)
                .errCode(reportSpec.code)
                .msg(reportSpec.msg)
                .logId(reportSpec.logId)
                .build();
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void checkExpiredRecord() {
        boolean check = eventReportRecord.containsKey("stub");
        if (!check) {
            log.info("expired report record checked");
        }
    }

    /**
     * a specification to perform a event report
     */
    @Getter(AccessLevel.PRIVATE)
    public class EventReportSpec {

        /**
         * 告警级别
         */
        private EventLevel level = EventLevel.HINT;

        /**
         * 接口名称
         */
        private String requestName;

        /**
         * 日志id
         */
        private String logId;

        /**
         * 错误码
         */
        private String code;

        /**
         * 错误信息
         */
        private String msg;

        public String getKeyword() {
            return StringUtils.joinWith("|", level.getValue(), requestName, code, msg);
        }

        /**
         * 发送事件通知
         *
         * @return 事件通知返回的响应Mono
         */
        public Mono<? extends Response> sendAndResponse() {
            if (StringUtils.isBlank(logId)) {
                logId = MDC.get(Constants.MDC_LOG_ID_KEY);
            }
            return sendReport(this);
        }

        public void send() {
            sendAndResponse()
                    .doOnError(e -> {
                        log.error("告警异常: {}", e.getMessage());
                    })
                    .subscribe(resp -> {
                        if (resp.isSuccess() && log.isDebugEnabled()) {
                            log.debug("告警成功");
                        }
                    });
        }

        public EventReportSpec level(EventLevel level) {
            this.level = level;
            return this;
        }

        public EventReportSpec requestName(String requestName) {
            this.requestName = requestName;
            return this;
        }

        public EventReportSpec logId(String logId) {
            this.logId = logId;
            return this;
        }

        public EventReportSpec code(String code) {
            this.code = code;
            return this;
        }

        public EventReportSpec msg(String msg) {
            this.msg = msg;
            return this;
        }

        public EventReportSpec cause(Throwable e) {
            this.code = e instanceof BaseException ? ((BaseException) e).getCode() : ExceptionCodes.SYSTEM_EXCEPTION;
            this.msg = e instanceof SystemException ? ((SystemException) e).getEventMessage() : e.getMessage();
            return this;
        }
    }

}
