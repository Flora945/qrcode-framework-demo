package com.jlpay.qrcode.external.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jlpay.qrcode.external.commons.io.IoUtil;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

/**
 * @author qihuaiyuan
 * @since 2021/05/15
 */
@Getter
@Setter
public abstract class AbstractBusiProcessContext implements BusiProcessContext {

    protected String rawRequest;

    protected JSONObject parsedRequest;

    protected String requestName;

    protected String logId;

    protected boolean completed = false;

    protected final long startTimestamp;

    public AbstractBusiProcessContext() {
        startTimestamp = System.currentTimeMillis();
    }

    @Override
    public JSONObject getParsedRequest() {
        if (parsedRequest == null) {
            parsedRequest = JSON.parseObject(getRawRequest(), JSONObject.class);
            if (parsedRequest == null) {
                parsedRequest = new JSONObject();
            }
        }
        return parsedRequest;
    }

    @Override
    public void completeProcess() {
        completed = true;
    }
}
