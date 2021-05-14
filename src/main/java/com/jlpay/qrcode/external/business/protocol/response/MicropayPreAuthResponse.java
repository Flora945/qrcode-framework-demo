package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class MicropayPreAuthResponse extends MicropayResponse {

    /**
     * 担保交易授权码
     */
    @JSONField(name = "guarantee_auth_code")
    private String guaranteeAuthCode;
}
