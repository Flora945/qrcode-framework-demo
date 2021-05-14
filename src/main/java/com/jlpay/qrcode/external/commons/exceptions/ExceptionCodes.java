package com.jlpay.qrcode.external.commons.exceptions;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public final class ExceptionCodes {

    private ExceptionCodes() {
        throw new UnsupportedOperationException("This class is not instantiable");
    }

    public static final String NETWORK_EXCEPTION = "98";

    public static final String SYSTEM_EXCEPTION = "96";

    public static final String CHANNEL_EXCEPTION = "99";

    public static final String BAD_REQUEST_PARAM = "30";
}
