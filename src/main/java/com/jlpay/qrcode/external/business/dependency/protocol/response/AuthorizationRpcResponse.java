package com.jlpay.qrcode.external.business.dependency.protocol.response;

import lombok.Data;

@Data
public class AuthorizationRpcResponse {
    private String retCode;
    private String retMsg;
    private String chnRetCode;
    private String chnRetMsg;
}
