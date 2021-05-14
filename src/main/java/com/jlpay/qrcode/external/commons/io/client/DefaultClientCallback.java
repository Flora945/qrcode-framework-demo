package com.jlpay.qrcode.external.commons.io.client;

import lombok.Builder;

import java.util.function.Consumer;

/**
 * default implementation
 *
 * @param <R> response type
 * @author qihuaiyuan
 * @since 2021/05/04
 */
@Builder
public class DefaultClientCallback<R> implements ClientCallback<R> {

    @Builder.Default
    private Consumer<R> responseConsumer = r -> {
    };

    @Builder.Default
    private Consumer<Throwable> exceptionConsumer = e -> {
    };

    @Override
    public void onResponse(R response) {
        responseConsumer.accept(response);
    }

    @Override
    public void onException(Throwable e) {
        exceptionConsumer.accept(e);
    }
}