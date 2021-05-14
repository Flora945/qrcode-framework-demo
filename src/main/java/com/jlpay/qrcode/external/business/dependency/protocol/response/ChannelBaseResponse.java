package com.jlpay.qrcode.external.business.dependency.protocol.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.api.utils.ApiConstants;
import lombok.Data;

@Data
public class ChannelBaseResponse {
    /**返回码*/
    @JSONField(name = "ret_code")
    private String retCode;
    /**返回信息*/
    @JSONField(name = "ret_msg")
    private String retMsg;

    /**订单号*/
    @JSONField(name = "order_id")
    private String orderId;
    /**交易状态  0-初始订单；1-待确认订单，s-交易成功，f-交易失败，c-交易已撤销，r-交易已退款**/
    private String status;
    /**渠道订单号*/
    @JSONField(name = "chn_order_id")
    private String chnOrderId;
    /**渠道返回码*/
    @JSONField(name = "chn_ret_code")
    private String chnRetCode;
    /**渠道返回信息*/
    @JSONField(name = "chn_ret_msg")
    private String chnRetMsg;
    /**交易金额*/
    private String amount;

    /**入账类型: 1: 全额入账, 2:以应结订单金额入账*/
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
    /**优惠活动名称*/
    @JSONField(name = "discount_name")
    private String discountName;
    /**应结订单金额*/
    @JSONField(name = "settlement_amt")
    private String settlementAmt;
    /**优惠信息*/
    @JSONField(name = "coupon_info")
    private String couponInfo;
    /**支付宝渠道返回的费率活动标识.当交易享受活动优惠费率时，返回该活动的标识；<br>
     * (1)蓝海活动优惠费率 0，值为 bluesea_1 <br>
     * (2) 特 殊 行 业 优 惠 费 率 0 ， 值 为industry_special_0 <br>
     * (3)特殊行业优惠费率千一，值为industry_special_01
     * */
    @JSONField(name = "charge_flags")
    private String chargeFlags;

    /**子商户openId*/
    @JSONField(name = "sub_open_id")
    private String subOpenId;
    /**交易类型*/
    @JSONField(name = "pay_type")
    private String payType;

    @JSONField(name = "auth_code")
    private String authCode;

    /**二维码状态:<br/>
     * 1:已扫码
     * */
    @JSONField(name = "qrcode_state")
    private String qrcodeState;

    /**渠道响应参数*/
    @JSONField(name = "channel_response_params")
    private JSONObject channelResponseParams;

    /**单品营销优惠信息*/
    @JSONField(name = "dct_goods_info")
    private String dctGoodsInfo;

    /**
     * 判断渠道是否成功
     * @return true:成功
     */
    public boolean isSuccess(){
        return ApiConstants.SUCCESS_CODE.equals(retCode);
    }
}
