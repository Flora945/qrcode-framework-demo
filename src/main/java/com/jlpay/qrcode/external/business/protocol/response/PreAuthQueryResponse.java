package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PreAuthQueryResponse extends MicropayResponse {

    @JSONField(name = "guarantee_auth_code")
    private String guaranteeAuthCode;
}
