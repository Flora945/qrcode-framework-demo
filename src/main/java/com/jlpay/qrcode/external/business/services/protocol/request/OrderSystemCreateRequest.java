package com.jlpay.qrcode.external.business.services.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class OrderSystemCreateRequest extends OrderSystemBaseReqeust {
    private String requestId;
    private String merchNo;
    private String busiType;
    private String busiSubType;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date transTime;
    private String amount;
    private String body;
}
