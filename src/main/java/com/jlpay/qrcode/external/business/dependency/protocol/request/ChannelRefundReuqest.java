package com.jlpay.qrcode.external.business.dependency.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChannelRefundReuqest extends ChannelBaseRequest {

    /**订单号*/
    @JSONField(name = "ori_order_id")
    private String oriOrderId;
    /**原交易订单金额*/
    @JSONField(name = "ori_amount")
    private String oriAmount;

    /**交易IP*/
    @JSONField(name = "trans_ip")
    private String transIp;

    @JSONField(name = "ret_code")
    private String retCode;

    @JSONField(name = "ret_msg")
    private String retMsg;
}
