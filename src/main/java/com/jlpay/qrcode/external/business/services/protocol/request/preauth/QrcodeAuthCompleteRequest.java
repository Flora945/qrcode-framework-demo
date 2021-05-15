package com.jlpay.qrcode.external.business.services.protocol.request.preauth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 码付预授权完成请求接口协议
 *
 * Created by wangyuhai1 on 2019/10/31.
 */
@Data
public class QrcodeAuthCompleteRequest extends QrcodeAuthBaseRequest {

    public static final String COMMAND_ID = "QrcodeAuthComplete";

    @JSONField(name = "command_id")
    private String commandId = COMMAND_ID;

    @JSONField(name = "ori_order_id")
    private String oriOrderId;

    @JSONField(name = "source")
    private String source;

    @JSONField(name = "auth_code")
    private String authCode;
}
