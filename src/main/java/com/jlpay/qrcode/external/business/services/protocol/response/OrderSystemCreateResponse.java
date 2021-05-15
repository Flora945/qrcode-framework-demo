package com.jlpay.qrcode.external.business.services.protocol.response;

import lombok.Data;

@Data
public class OrderSystemCreateResponse extends OrderSystemBaseResponse {
    private String requestId;
    private String orderId;
}
