package com.jlpay.qrcode.external.commons.exceptions.def;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public class ConnectException extends NetworkException {

    public ConnectException(String message, String eventMessage) {
        super(message, eventMessage);
    }

    public ConnectException(String message, String eventMessage, Throwable cause) {
        super(message, eventMessage, cause);
    }
}
