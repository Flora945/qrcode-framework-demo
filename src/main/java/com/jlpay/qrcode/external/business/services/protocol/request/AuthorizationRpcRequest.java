package com.jlpay.qrcode.external.business.services.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AuthorizationRpcRequest {

    @JSONField(name = "command_id")
    private String commandId;
    @JSONField(name = "pay_type")
    public String payType;
    @JSONField(name = "app_id")
    public String appId;
    /**商户号*/
    @JSONField(name = "merch_no")
    public String merchNo;
    @JSONField(name = "ip")
    private String ip;
    /**绑定的支付目录*/
    @JSONField(name = "jsapi_path")
    private String jsapiPath;
    /**绑定的appid*/
    @JSONField(name = "sub_appid")
    private String subAppid;
    /**推荐关注公众号/小程序appid.已停用*/
    @JSONField(name = "subscribe_appid")
    private String subscribeAppid;
    /**来源:8001*/
    private String source;
}
