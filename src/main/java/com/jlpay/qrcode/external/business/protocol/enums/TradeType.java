package com.jlpay.qrcode.external.business.protocol.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付类型
 *
 * @author zhaoyang2
 * @author qihuaiyuan
 */
@Getter
public enum TradeType {

    /**
     * 条形码
     */
    AUTHCODE("条形码", "0"),
    /**
     * 扫码
     */
    QRCODE("扫码", "1", true),
    /**
     * 公众号或服务窗
     */
    OFFACCOUNTS("公众号或服务窗", "2", true),
    /**
     * 银行卡
     */
    CARD("银行卡", "3"),
    /**
     * APP
     */
    APP("APP", "4", true),
    /**
     * 人脸支付
     */
    FACEPAY("人脸支付", "5", true),
    /**
     * 三码合一,payType为jlpay
     */
    NATIVE_PACKAGE("三码合一", "6", true);

    /**
     * 中文描述
     */
    private final String desc;
    /**
     * 值
     */
    private final String value;
    /**
     * 主扫
     */
    private final boolean c2bQrcode;

    private static final Map<String, TradeType> MAP = new HashMap<>();

    static {
        for (TradeType tradeType : values()) {
            MAP.put(tradeType.value, tradeType);
        }
    }

    public static TradeType forValue(String value) {
        return MAP.get(value);
    }

    TradeType(String desc, String value, boolean c2bQrcode) {
        this.desc = desc;
        this.value = value;
        this.c2bQrcode = c2bQrcode;
    }

    TradeType(String desc, String value) {
        this.desc = desc;
        this.value = value;
        c2bQrcode = false;
    }

}
