package com.jlpay.qrcode.external.business.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jlpay.qrcode.external.business.services.protocol.request.timedquery.AddTimedQueryTaskRequest;
import com.jlpay.qrcode.external.business.services.protocol.request.timedquery.BackwardQueryRequest;
import com.jlpay.qrcode.external.commons.events.EventReporter;
import com.jlpay.qrcode.external.commons.exceptions.assertion.BusiAssert;
import com.jlpay.qrcode.external.commons.io.client.ReactiveHttpTool;
import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import com.jlpay.qrcode.external.config.properties.TransTimedQueryProperties;
import com.jlpay.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * 定时查询服务
 *
 * @author qihuaiyuan
 * @since 2020-09-22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TimedQueryService {

    private static final String QUERY_ID_PREFIX = "EXT_QR_";

    private final TransTimedQueryProperties properties;

    private final ReactiveHttpTool httpTool;

    private final EventReporter eventReporter;

    /**
     * 添加延迟查询任务
     * 发生异常或提交失败时发送告警
     *
     * @param orderId 订单号
     */
    public void addTimedQueryTask(String orderId) {
        AddTimedQueryTaskRequest request = new AddTimedQueryTaskRequest();
        request.setUri(properties.getBackwardQueryUrl());
        request.setFirstDelayLevel(properties.getInitialDelayLevel());
        request.setIntervalDelayLevel(properties.getIntervalDelayLevel());
        request.setLogId(MDC.get(Constants.LOGID));
        request.setTimes(properties.getMaxQueryTimes());
        request.setQueryId(QUERY_ID_PREFIX + orderId);
        BackwardQueryRequest queryRequest = new BackwardQueryRequest();
        queryRequest.setOrderId(orderId);
        request.setContent(JSON.toJSONString(queryRequest));

        httpTool.preparePost()
                .url(properties.getAddQueryTaskUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .requestBody(request)
                .requestTimeout(properties.getTimeout())
                .execute()
                .doOnError(e -> handleException(orderId, e))
                .subscribe(result -> {
                    log.info("补偿查询响应: {}", result);
                    DefaultResponse response = JSONObject.parseObject(result, DefaultResponse.class);
                    BusiAssert.isTrue(response.isSuccess(), response.getRetCode(), result);
                }, e -> handleException(orderId, e));
    }

    private void handleException(String orderId, Throwable e) {
        log.error("提交延迟查询异常", e);
        eventReporter.prepareReport()
                .requestName("OUT_QR_ADD_TIMED_QUERY")
                .cause(e)
                .msg(orderId + "---" + e.getMessage())
                .send();
    }

}
