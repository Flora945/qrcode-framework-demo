package com.jlpay.qrcode.external.business.dependency.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChannelAppPayRequest extends com.jlpay.qrcode.api.protocol.channel.request.ChannelBaseRequest {
    @JSONField(name = "ip")
    private String ip;
    @JSONField(name = "amount")
    private String amount;
    @JSONField(name = "notify_url")
    private String notifyUrl;
    @JSONField(name = "subject")
    private String subject;
    @JSONField(name = "wechatAppid")
    private String wechatAppid;
    @JSONField(name = "userId")
    private String userId;
}
