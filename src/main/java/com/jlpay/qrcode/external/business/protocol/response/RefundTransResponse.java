package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class RefundTransResponse extends BaseTransResponse {

    /**商家原交易订单号*/
    @JSONField(name = "ori_out_trade_no")
    private String oriOutTradeNo;

    /**嘉联原交易订单号*/
    @JSONField(name = "ori_transaction_id")
    private String oriTransactionId;

    /**优惠信息*/
    @JSONField(name = "coupon_Info")
    private String couponInfo;

    /**单品营销优惠信息*/
    @JSONField(name = "dct_goods_info")
    private String dctGoodsInfo;

}
