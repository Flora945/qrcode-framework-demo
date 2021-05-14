package com.jlpay.qrcode.external.business.dependency.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChannelRefundResponse extends com.jlpay.qrcode.api.protocol.channel.response.ChannelBaseResponse {

    /**原交易订单号*/
    @JSONField(name = "ori_order_id")
    private String oriOrderId;
}
