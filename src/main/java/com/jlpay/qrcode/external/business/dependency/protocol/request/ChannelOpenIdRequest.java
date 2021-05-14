package com.jlpay.qrcode.external.business.dependency.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChannelOpenIdRequest extends com.jlpay.qrcode.api.protocol.channel.request.ChannelBaseRequest {

    /**	通过授权码查询公众号Openid，调用查询后，该授权码只能由此商户号发起扣款，直至授权码更新*/
    @JSONField(name = "sub_app_id")
    private String subAppId;
    /**微信授权码,银联用户授权码*/
    @JSONField(name = "auth_code")
    private String authCode;
    /**银联支付标识,银联二维码必填*/
    @JSONField(name = "app_up_identifier")
    private String appUpIdentifier;

}
