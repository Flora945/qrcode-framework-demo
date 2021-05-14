package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class OpenIdResponse extends BaseTransResponse {

    /**嘉联appid下对应的openId*/
    @JSONField(name = "open_id")
    private String openId;
    /**子商户openid*/
    @JSONField(name = "sub_open_id")
    private String subOpenId;
    /**银联用户标识*/
    @JSONField(name = "user_id")
    private String userId;
}
