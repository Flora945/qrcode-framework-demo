package com.jlpay.qrcode.external.business.dependency.protocol.response.preauth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 担保应答协议基类
 * Created by wangyuhai1 on 2019/11/5.
 */
@Data
public class QrcodeAuthBaseResponse {
    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "ret_code")
    private String retCode;

    @JSONField(name = "ret_msg")
    private String retMsg;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "pay_type")
    private String payType;

    public boolean isSuccess() {
        return "00".equals(retCode);
    }
}
