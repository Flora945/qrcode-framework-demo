package com.jlpay.qrcode.external.commons.io.client;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public interface Client<R> {

    /**
     * perform a client request
     *
     * @param callback callback handler
     */
    void performRequest(ClientCallback<R> callback);
}
