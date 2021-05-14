package com.jlpay.qrcode.external.commons.exceptions.def;

import lombok.Getter;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
public class BaseException extends RuntimeException {

    private final String code;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
