package com.jlpay.qrcode.external.business.services.protocol.response;

import lombok.Data;

@Data
public class AuthorizationRpcResponse {
    private String retCode;
    private String retMsg;
    private String chnRetCode;
    private String chnRetMsg;
}
