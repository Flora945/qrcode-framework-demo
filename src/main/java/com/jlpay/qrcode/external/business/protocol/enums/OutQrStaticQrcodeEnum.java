package com.jlpay.qrcode.external.business.protocol.enums;

import lombok.Getter;

/**
 * 静态码表字段枚举值
 *
 * @author qihuaiyuan
 * @since 2019/9/3 16:47
 */
@Getter
public enum OutQrStaticQrcodeEnum {
    /**
     * 状态: 成功
     */
    STATUS_SUCCESS("1"),
    /**
     * 状态: 失败
     */
    STATUS_FAIL("0"),
    /**
     * 类型: NFC标签
     */
    CODE_TYPE_NFC("02"),
    /**
     * 类型: 银联静态码
     */
    CODE_TYPE_STATIC_CODE("1")
    ;

    private String value;

    OutQrStaticQrcodeEnum(String value) {
        this.value = value;
    }
}
