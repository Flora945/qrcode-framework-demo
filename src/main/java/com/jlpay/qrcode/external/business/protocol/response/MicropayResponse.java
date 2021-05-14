package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class MicropayResponse extends BaseTransResponse {

    /**终端号*/
    @JSONField(name = "term_no")
    private String termNo;
    /**设备号 否 String(32) 终端设备号*/
    @JSONField(name = "device_info")
    private String deviceInfo;

    /**实际付款金额*/
    @JSONField(name = "finnal_amount")
    private String finnalAmount;
    /**优惠总金额*/
    @JSONField(name = "discount_amount")
    private String discountAmount;
    /**优惠活动名称*/
    @JSONField(name = "discount_name")
    private String discountName;
    /**优惠信息*/
    @JSONField(name = "coupon_Info")
    private String couponInfo;

    /**sub_openid 在sub-appid下的openid*/
    @JSONField(name = "sub_openid")
    private String subOpenid;

    //订单查询返回字段
    /**备注*/
    private String remark;
    /**操作员*/
    @JSONField(name = "op_user_id")
    private String opUserId;
    /**门店号*/
    @JSONField(name = "op_shop_id")
    private String opShopId;
    /**退款详情*/
    @JSONField(name = "refund_list")
    private List<JSONObject> refundList;
    /**退款次数*/
    private Integer count;
    /**单品营销优惠信息*/
    @JSONField(name = "dct_goods_info")
    private String dctGoodsInfo;
}
