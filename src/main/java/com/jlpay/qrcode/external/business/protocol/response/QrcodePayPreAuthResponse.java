package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * C2B 主扫预授权交易
 *
 * @author qihuaiyuan
 * @date 2019/12/25 16:01
 */
@Data
public class QrcodePayPreAuthResponse extends QrcodePayResponse{

    @JSONField(name = "guarantee_auth_code")
    private String guaranteeAuthCode;
}
