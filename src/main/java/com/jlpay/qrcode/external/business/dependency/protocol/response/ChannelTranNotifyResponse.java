package com.jlpay.qrcode.external.business.dependency.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import lombok.Data;

import java.util.Date;

@Data
public class ChannelTranNotifyResponse extends DefaultResponse {

    /**
     * 渠道响应码
     */
    private String chnRetCode;

    /**
     * 渠道响应信息
     */
    private String chnRetMsg;

    /**渠道号*/
    @JSONField(name = "channel_id")
    private String channelId;

    /**
     * 回调类型<br/>
     * NFC: NFC支付回调<br/>
     * STATIC_CODE: 静态码支付
     */
    @JSONField(name = "notify_type")
    private String notifyType;
    /**订单号*/
    @JSONField(name = "order_no")
    private String orderNo;
    /**原始订单号*/
    @JSONField(name = "ori_order_no")
    private String oriOrderNo;
    /**渠道订单号*/
    @JSONField(name = "chn_order_no")
    private String chnOrderNo;

    private String status;

    /**业务大类*/
    @JSONField(name = "busi_type")
    private String busiType;

    /**0:非全额入账,<br>
     * 1:全额入账,<br>
     * 2:以应结订单金额入账.<br>
     * 码付只会返回1:全额入账  和2:以应结订单金额入账
     * */
    @JSONField(name = "discount_settle_type")
    private String discountSettleType;
    /**实付金额*/
    @JSONField(name = "finnal_amount")
    private String finnalAmount;
    /**优惠金额*/
    @JSONField(name = "discount_amount")
    private String discountAmount;
    /**额外优惠金额*/
    @JSONField(name = "exter_discount_amount")
    private String exterDiscountAmount;

    private String amount;

    /**优惠活动名称*/
    @JSONField(name = "discount_name")
    private String discountName;
    /**应结订单金额,单位分*/
    @JSONField(name = "settlement_amt")
    private String settlementAmt;
    /**优惠信息,成功时出现。使用优惠活动时出现*/
    @JSONField(name = "coupon_Info")
    private String couponInfo;
    /**支付宝渠道返回的费率活动标识*/
    @JSONField(name = "charge_flags")
    private String chargeFlags;

    /**清算主键*/
    private String settleKey;
    /**服务点条件码*/
    private String posConditionCode;
    /**检索参考号*/
    private String referNo;
    /**结算日期*/
    private String settleDate;
    @JSONField(name = "pay_info")
    private String payInfo;
    /**用户ID*/
    private String subAppId;
    /**交易银行名称*/
    private String bankName;
    /**交易卡类型*/
    private String cardType;

    /**单品优惠信息*/
    @JSONField(name = "dct_goods_info")
    private String dctGoodsInfo;
    /**买家付款金额,单位分*/
    @JSONField(name = "pay_amt")
    private String payAmt;
    /**交易支付时间,格式:20180629112830*/
    @JSONField(name = "pay_time", format = "yyyyMMddHHmmss")
    private Date payTime;
    /**支付数据,调用微信app,网页支付等的必要数据*/
    @JSONField(name = "pay_data")
    private String payData;
    /**子商户 appid 下用户唯一标识*/
    @JSONField(name = "sub_open_id")
    private String subOpenId;

}
