package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Description: 回调通知
 * @author: lvlinyang
 * @date: 2019/10/23 14:23
 */
@Data
public class TransNotifyResponse extends BaseTransResponse{

    @JSONField(name = "notify_url")
    private String notifyUrl;

    @JSONField(name = "term_no")
    private String termNo;
    @JSONField(name = "device_info")
    private String deviceInfo;

    /**操作员 否 String(32) 操作员帐号,默认为商户号*/
    @JSONField(name = "op_user_id")
    private String opUserId;
    /**门店编号 否 String(32)*/
    @JSONField(name = "op_shop_id")
    private String opShopId;
    /**实际付款金额*/
    @JSONField(name = "finnal_amount")
    private String finnalAmount;
    /**优惠总金额*/
    @JSONField(name = "discount_amount")
    private String discountAmount;
    /**优惠活动名称*/
    @JSONField(name = "discount_name")
    public String discountName;
    /**优惠信息*/
    @JSONField(name = "coupon_Info")
    private String couponInfo;

    @JSONField(name = "remark")
    private String remark;

    /**sub_openid 在sub-appid下的openid*/
    @JSONField(name = "sub_openid")
    private String subOpenid;
    /**来源*/
    private String source;
    /**单品营销优惠信息*/
    @JSONField(name = "dct_goods_info")
    private String dctGoodsInfo;
}
