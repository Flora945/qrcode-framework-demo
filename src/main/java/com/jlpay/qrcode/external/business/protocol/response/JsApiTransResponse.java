package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class JsApiTransResponse extends BaseTransResponse {

    /**终端号*/
    @JSONField(name = "term_no")
    private String termNo;
    /**设备号*/
    @JSONField(name = "device_info")
    private String deviceInfo;

    /**支付信息*/
    @JSONField(name = "pay_info")
    private String payInfo;
}
