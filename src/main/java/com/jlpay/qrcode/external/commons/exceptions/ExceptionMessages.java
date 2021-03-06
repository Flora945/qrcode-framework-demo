package com.jlpay.qrcode.external.commons.exceptions;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public final class ExceptionMessages {

    private ExceptionMessages() {
        throw new UnsupportedOperationException("This class is not instantiable");
    }

    public static final String SYSTEM_EXCEPTION_MESSAGE = "系统异常";

    public static final String TRANSACTION_TIMEOUT_RETRY = "交易超时,请重试";
}
