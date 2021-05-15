package com.jlpay.qrcode.external.business.services.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChannelAppPayResponse extends ChannelBaseResponse {
    @JSONField(name = "chn_order_no")
    private String chnOrderNo;
    @JSONField(name = "order_time")
    private String orderTime;
    @JSONField(name = "prepayId")
    private String prepayId;
    @JSONField(name = "partnerId")
    private String partnerId;
}
