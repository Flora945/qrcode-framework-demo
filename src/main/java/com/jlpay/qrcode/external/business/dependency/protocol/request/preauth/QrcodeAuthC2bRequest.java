package com.jlpay.qrcode.external.business.dependency.protocol.request.preauth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 码付预授权(C2B)请求接口协议
 * Created by wangyuhai1 on 2019/11/5.
 */
@Data
public class QrcodeAuthC2bRequest extends QrcodeAuthBaseRequest {

    public static final String COMMAND_ID = "QrcodeAuthC2b";

    /**命令字**/
    @JSONField(name = "command_id")
    private String commandId = COMMAND_ID;

    @JSONField(name = "guarantee_expiry")
    private String guaranteeExpiry;

    @JSONField(name = "qrcode_expiry")
    private String qrcodeExpiry;

}
