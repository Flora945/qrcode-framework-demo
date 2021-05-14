package com.jlpay.qrcode.external.commons.exceptions.def;


import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public class NetworkException extends SystemException {

    public NetworkException(String message, String eventMessage) {
        super(ExceptionCodes.NETWORK_EXCEPTION, message, eventMessage);
    }

    public NetworkException(String message, String eventMessage, Throwable cause) {
        super(ExceptionCodes.NETWORK_EXCEPTION, message, eventMessage, cause);
    }
}
