package com.jlpay.qrcode.external.business.dependency.protocol.response.preauth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 默认应答
 *
 * Created by wangyuhai1 on 2019/10/31.
 */
@Data
public class QrcodeAuthC2bResponse extends QrcodeAuthBaseResponse {
    @JSONField(name = "qrcode")
    private String qrcode;
    @JSONField(name = "auth_code")
    private String authCode;
}
