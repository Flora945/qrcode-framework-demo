package com.jlpay.qrcode.external.business.protocol.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务小类
 *
 * @author zhaoyang2
 * @author qihuaiyuan
 */
@Getter
public enum BusiSubType {
	/**B2C条码支付*/
	B2C_MICROPAY("B2C条码支付","1537",false,"SALE"),
	/**B2C条码支付退货*/
    B2C_MICROPAY_REFUND("B2C条码支付退货","1538",false,"REFUND"),
	/**B2C条码支付撤消*/
    B2C_MICROPAY_CANCEL("B2C条码支付撤消","1539",false,"VSALE"),
    /**B2C条码支付冲正*/
    B2C_MICROPAY_REVERSE("B2C条码支付冲正","1540",false,"VSALE"),
    /**B2C条码支付撤消冲正*/
    B2C_MICROPAY_CANCEL_REVERSE("B2C条码支付撤消冲正","1541",false,"VSALE"),
    /**B2C条码手工退款*/
    B2C_MICROPAY_MANUAL_REFUND("B2C条码手工退款","1542",false,"REFUND"),

    /**C2B扫码支付*/
    C2B_NATIVEPAY("C2B扫码支付","1551",false,"SALE"),
    /**C2B扫码支付退货*/
    C2B_NATIVEPAY_REFUND("C2B扫码支付退货","1552",false,"REFUND"),
    /**C2B扫码支付撤销*/
    C2B_NATIVEPAY_CANCEL("C2B扫码支付撤销","1553",false,"VSALE"),
    /**C2B扫码支付冲正*/
    C2B_NATIVEPAY_REVERSE("C2B扫码支付冲正","1554",false,"VSALE"),
    /**C2B扫码支付撤销冲正*/
    C2B_NATIVEPAY_CANCEL_REVERSE("C2B扫码支付撤销冲正","1555",false,"VSALE"),

    /**
     * 码付预授权交易
     */
    B2C_MICROPAY_PRE_AUTHORIZATION("B2C(被扫)担保交易", "1581", true,"AUTH"),
    C2B_NATIVEPAY_PRE_AUTHORIZATION("C2B(主扫)担保交易", "1582", true,"AUTH"),
    PRE_AUTHORIZATION_COMPLETE("担保完成", "1583", true,"CAUTH"),
    PRE_AUTHORIZATION_CANCEL("担保撤销", "1584", true,"VAUTH"),
    PRE_AUTHORIZATION_TRADE_CLOSE("担保关单", "1588", true,"VSALE"),


    /**公众号支付消费*/
	JSPAY("公众号支付消费","1557",false,"SALE"),
    /**公众号支付退货*/
	JSPAY_REFUND("公众号支付退货","1558",false,"REFUND"),
    /**公众号支付撤销*/
	JSPAY_CANCEL("公众号支付撤销","1559",false,"VSALE"),

    /**APP支付*/
	APP("APP支付","1564",false,"SALE"),
    /**APP退款*/
	APP_REFUND("APP退款","1565",false,"REFUND"),

    /**分期B2C条码支付*/
	FC_B2C_PAY("分期B2C条码支付", "1510",false,"SALE"),
    /**分期B2C条码支付退货*/
    FC_B2C_REFUND("分期B2C条码支付退货", "1511",false,"REFUND"),
    /**分期C2B扫码支付*/
    FC_C2B_PAY("分期C2B扫码支付", "1517",false,"SALE"),
    /**分期C2B扫码支付退货*/
    FC_C2B_REFUND("分期C2B扫码支付退货", "1518",false,"REFUND"),
    /**分期服务窗支付*/
    FC_FWC_PAY("分期服务窗支付", "1523",false,"SALE"),
    /**分期服务窗支付退货*/
    FC_FWC_REFUND("分期服务窗支付退货", "1524",false,"REFUND"),

    NFC_PAY("NFC标签支付","1567",false,"SALE"),

    STATIC_CODE("银联静态码","2561",false,"SALE");
	
	private final String desc;
	private final String flag;

    /**
     * 码付预授权交易
     */
	private final boolean preAuthTrade;
    /**订单系统交易类型*/
	private final String orderSystemType;

    private static final Map<String, BusiSubType> MAP = new HashMap<>();

    static {
        for (BusiSubType busiSubType : values()) {
            MAP.put(busiSubType.flag, busiSubType);
        }
    }

    public static BusiSubType forValue(String value) {
        return MAP.get(value);
    }

    BusiSubType(String desc, String flag, boolean preAuthTrade, String orderSystemType) {
        this.desc = desc;
        this.flag = flag;
        this.preAuthTrade = preAuthTrade;
        this.orderSystemType = orderSystemType;
    }
}
