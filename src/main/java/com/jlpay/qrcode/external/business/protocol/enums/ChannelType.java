package com.jlpay.qrcode.external.business.protocol.enums;

/**
 * @Description: 渠道类型枚举
 * @author: lvlinyang
 * @date: 2019/11/1 20:19
 */
public enum ChannelType {

    /**银联支付宝*/
    UNIONPAY_ALIPAY("银联支付宝","10009","alipay"),
    /**银联微信*/
    UNIONPAY_WXPAY("银联微信","10011","wxpay"),
    /**银联二维码*/
    UNIONPAY_QUICKPASS("银联二维码","10005","unionpay"),
    /**网联支付宝*/
    NETPAY_ALIPAY("网联支付宝","10018","alipay"),
    /**网联微信*/
    NETPAY_WXPAY("网联微信","10017","wxpay"),
    /**未知*/
    UNKNOW("","","");

    private String desc;
    private String channelId;
    private String payType;

    ChannelType(String desc, String channelId, String payType) {
        this.desc = desc;
        this.channelId = channelId;
        this.payType = payType;
    }

    public String getDesc() {
        return desc;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getPayType() {
        return payType;
    }

    /**
     * 根据渠道号查出对应的类型
     * @param channelId
     * @return
     */
    public static ChannelType findByChannelId(String channelId){
        for (ChannelType channelType : ChannelType.values()){
            if (channelType.channelId.equals(channelId)){
                return channelType;
            }
        }
        return UNKNOW;
    }
}
