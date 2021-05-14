package com.jlpay.qrcode.external.business.protocol.enums;

/**
 * @Description 交易类型
 * @Author zhaoyang
 * @Date 2019/10/22 21:31
 **/
public enum TransType {

    WXPAY_OFFICIALACCPAY("微信公众号","wxpay", "pay.qrcode.officialpay", "1"),
    WXPAY_QRCODEPAY("微信扫码","wxpay", "pay.qrcode.qrcodepay", "2"),
    WXPAY_MICROPAY("微信刷卡支付","wxpay", "pay.qrcode.micropay", "3"),
    WXPAY_MICROPAYASYN("微信刷卡支付","wxpay", "pay.qrcode.micropayasyn", "3"),
    ALIPAY_QRCODEPAY("支付宝扫码","alipay", "pay.qrcode.qrcodepay", "4"),
    ALIPAY_MICROPAY("支付宝刷卡","alipay", "pay.qrcode.micropay", "5"),
    ALIPAY_MICROPAYASYN("支付宝刷卡","alipay", "pay.qrcode.micropayasyn", "5"),
    ALIPAY_WAPH5PAY("支付宝服务窗","alipay", "pay.qrcode.waph5pay", "6"),
    UNIONPAY_JSPAY("银联行业码","unionpay", "pay.qrcode.unionjspay", "7"),
    UNIONPAY_QRCODEPAY("银联扫码","unionpay", "pay.qrcode.qrcodepay", "8"),
    UNIONPAY_MICROPAY("银联刷卡","unionpay", "pay.qrcode.micropay", "9"),
    UNIONPAY_MICROPAYASYN("银联刷卡","unionpay", "pay.qrcode.micropayasyn", "9"),
    WXPAY_APPPAY("微信app支付","wxpay", "pay.qrcode.apppay", "10"),
    QQPAY_QRCODEPAY("qq扫码","qqpay", "pay.qrcode.qrcodepay", "11"),
    QQPAY_MICROPAY("qq刷卡支付","qqpay", "pay.qrcode.micropay", "12"),
    QQPAY_MICROPAYASYN("qq刷卡支付","qqpay", "pay.qrcode.micropayasyn", "12"),
    JLPAY_QRCODEPAY("嘉联聚合支付", "jlpay", "pay.qrcode.qrcodepay", "13"),
    UNKNOW("未知类型","", "", "");

    /**
     * 交易类型描述
     */
    private String desc;
    /**
     * 支付类型
     */
    private String payType;
    /**
     * 接口名称
     */
    private String serviceName;
    /**
     * 交易类型接口对应值
     */
    private String value;

    TransType(String desc, String payType, String serviceName, String value) {
        this.desc = desc;
        this.payType = payType;
        this.serviceName = serviceName;
        this.value = value;
    }

    /**
     * 通过支付类型和接口名称查询
     * @param payType
     * @param serviceName
     * @return
     */
    public static TransType findByPayTypeAndServiceName(String payType, String serviceName) {
        for (TransType type : TransType.values()) {
            if (type.payType.equals(payType) && type.serviceName.equals(serviceName)) {
                return type;
            }
        }
        return UNKNOW;
    }

    /**
     * 是否需要校验
     * @param serviceName
     * @return
     */
    public static boolean ifCheckService(String serviceName) {
        for (TransType type : TransType.values()) {
            if (type.serviceName.equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
