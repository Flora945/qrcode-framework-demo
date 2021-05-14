package com.jlpay.qrcode.external.commons.exceptions.def;

import lombok.Getter;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
public class HttpStatusNotOkException extends NetworkException {

    private final int status;

    public HttpStatusNotOkException(int status, String message, String eventMessage) {
        super(message, eventMessage);
        this.status = status;
    }
}
