package com.jlpay.qrcode.external.business.dependency.protocol.request.preauth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 码付预授权撤销请求接口协议
 *
 * Created by wangyuhai1 on 2019/10/31.
 */
@Data
public class QrcodeAuthCancelRequest extends QrcodeAuthBaseRequest {

    public static final String CHN_PRE_AUTH_REVOKE_COMMAND_ID = "QrcodeAuthCancel";

    public static final String CHN_PRE_AUTH_TRANS_CLOSE_COMMAND_ID = "QrcodeAuthClose";

    @JSONField(name = "command_id")
    private String commandId;

    @JSONField(name = "ori_order_id")
    private String oriOrderId;

    @JSONField(name = "source")
    private String source;

    @JSONField(name = "auth_code")
    private String authCode;

}
