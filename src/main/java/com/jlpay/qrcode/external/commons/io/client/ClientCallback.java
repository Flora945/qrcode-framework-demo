package com.jlpay.qrcode.external.commons.io.client;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public interface ClientCallback<R> {

    /**
     * create a ClientCallback builder
     * @param <R> response type
     * @return the builder
     */
    static <R> DefaultClientCallback.DefaultClientCallbackBuilder<R> builder() {
        return DefaultClientCallback.builder();
    }

    /**
     * handle response
     *
     * @param response response
     */
    void onResponse(R response);

    /**
     * handle exception
     * @param e exception
     */
    void onException(Throwable e);

}


