package com.jlpay.qrcode.external.controller;

import com.alibaba.fastjson.JSONObject;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public interface BusiProcessContext {

    String getRequestName();

    String getRawRequest();

    JSONObject getParsedRequest();

    void writeResponse(String response);

    void completeProcess();

    String getLogId();

    boolean isCompleted();
}
