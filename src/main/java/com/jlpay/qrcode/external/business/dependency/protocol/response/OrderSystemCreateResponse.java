package com.jlpay.qrcode.external.business.dependency.protocol.response;

import lombok.Data;

@Data
public class OrderSystemCreateResponse extends OrderSystemBaseResponse {
    private String requestId;
    private String orderId;
}
