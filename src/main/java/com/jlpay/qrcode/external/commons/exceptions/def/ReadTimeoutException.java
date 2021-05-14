package com.jlpay.qrcode.external.commons.exceptions.def;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public class ReadTimeoutException extends NetworkException {

    public ReadTimeoutException(String message, String eventMessage) {
        super(message, eventMessage);
    }

    public ReadTimeoutException(String message, String eventMessage, Throwable cause) {
        super(message, eventMessage, cause);
    }
}
