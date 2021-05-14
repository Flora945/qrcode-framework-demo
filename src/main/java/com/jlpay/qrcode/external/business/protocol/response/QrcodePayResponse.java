package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class QrcodePayResponse extends BaseTransResponse {

    /**终端号*/
    @JSONField(name = "term_no")
    private String termNo;
    /**设备号*/
    @JSONField(name = "device_info")
    private String deviceInfo;
    /**二维码*/
    @JSONField(name = "code_url")
    private String codeUrl;

    /**sub_openid 在sub-appid下的openid*/
    @JSONField(name = "sub_openid")
    private String subOpenid;

    /**NFC标签信息*/
    @JSONField(name = "nfc_tag")
    private String nfcTag;
}
