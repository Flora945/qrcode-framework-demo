package com.jlpay.qrcode.external.business.dependency.protocol.request.preauth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 退款请求接口协议
 *
 * Created by wangyuhai1 on 2019/10/31.
 */
@Data
public class QrcodeAuthRefundRequest extends QrcodeAuthBaseRequest {
    @JSONField(name = "ori_order_id")
    private String oriOrderId;

    @JSONField(name = "source")
    private String source;
    @JSONField(name = "auth_code")
    private String authCode;
}
