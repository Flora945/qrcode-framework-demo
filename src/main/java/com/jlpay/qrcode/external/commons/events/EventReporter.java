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
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
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
     * ιε€εεΊ
     */
    private static final DefaultResponse RESPONSE_FOR_DUPLICATED =
            DefaultResponse.fromCodeAndMsg(Constants.RET_CODE_SUCCESS, Constants.DUPLICATED_WARNING_MESSAGE);

    private final EventReportProperties properties;

    private final ReactiveHttpTool httpTool;

    private PassiveExpiringMap<String, Object> eventReportRecord;

    @PostConstruct
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
                log.debug("ιε€εθ­¦: {}", keyword);
            }
            return Mono.just(RESPONSE_FOR_DUPLICATED);
        }
        // build request
        EventReportRequest request = buildRequest(reportSpec);
        // send request
        return httpTool.preparePost()
                .url(properties.getRequestUrl())
                .logId(request.getLogId())
                .contentType(MediaType.APPLICATION_JSON)
                .requestBody(request)
                .requestTimeout(properties.getRequestTimeout())
                .execute()
                .map(resp -> JSON.parseObject(resp, DefaultResponse.class))
                .doOnNext(resp -> record(keyword, resp));

    }

    /**
     * θ?°ε½εθ­¦δ»₯ι²ι
     *
     * @param keyword  ε³ι?ε­
     * @param response εεΊ
     */
    private void record(String keyword, DefaultResponse response) {
        // εͺζεθ­¦ζεδΊζθ?°ε½
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
     * a specification to execute a event report
     */
    @Getter(AccessLevel.PRIVATE)
    public class EventReportSpec {

        /**
         * εθ­¦ηΊ§ε«
         */
        private EventLevel level = EventLevel.HINT;

        /**
         * ζ₯ε£εη§°
         */
        private String requestName;

        /**
         * ζ₯εΏid
         */
        private String logId;

        /**
         * ιθ――η 
         */
        private String code;

        /**
         * ιθ――δΏ‘ζ―
         */
        private String msg;

        public String getKeyword() {
            return StringUtils.joinWith("|", level.getValue(), requestName, code, msg);
        }

        /**
         * ειδΊδ»Άιη₯
         *
         * @return δΊδ»Άιη₯θΏεηεεΊMono
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
                        log.error("εθ­¦εΌεΈΈ: {}", e.getMessage());
                    })
                    .subscribe(resp -> {
                        if (resp.isSuccess() && log.isDebugEnabled()) {
                            log.debug("εθ­¦ζε");
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
