package com.jlpay.qrcode.external.business.dependency.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChannelQrcodeResponse extends com.jlpay.qrcode.api.protocol.channel.response.ChannelBaseResponse {

    /**二维码*/
    private String qrcode;

    /**标签信息*/
    @JSONField(name = "nfc_tag")
    private String nfcTag;
}
