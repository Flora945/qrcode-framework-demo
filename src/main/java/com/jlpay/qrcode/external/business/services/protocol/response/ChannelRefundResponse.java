package com.jlpay.qrcode.external.business.services.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChannelRefundResponse extends ChannelBaseResponse {

    /**原交易订单号*/
    @JSONField(name = "ori_order_id")
    private String oriOrderId;
}
