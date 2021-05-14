package com.jlpay.qrcode.external.business.dependency.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import lombok.Data;

@Data
public class ChannelOpenIdResponse extends DefaultResponse {

    /**微信subappid对应的subOpenId*/
    @JSONField(name = "sub_open_id")
    private String subOpenId;
    /**微信appid对应的openid*/
    @JSONField(name = "open_id")
    private String openId;
    /**渠道返回码*/
    @JSONField(name = "chnRetCode")
    private String chnRetCode;
    /**渠道返回信息*/
    @JSONField(name = "chnRetMsg")
    private String chnRetMsg;
    /**银联用户标识*/
    @JSONField(name = "user_id")
    private String userId;
}
