package com.jlpay.qrcode.external.business.services.protocol.request;

import lombok.Data;

@Data
public class OrderSystemBaseReqeust {
    private String commandId;
    private String sources;
}
