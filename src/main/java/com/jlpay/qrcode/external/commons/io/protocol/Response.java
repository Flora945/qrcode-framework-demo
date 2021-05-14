package com.jlpay.qrcode.external.commons.io.protocol;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public interface Response {

    String getRetCode();

    String getRetMsg();

    boolean isSuccess();
}
