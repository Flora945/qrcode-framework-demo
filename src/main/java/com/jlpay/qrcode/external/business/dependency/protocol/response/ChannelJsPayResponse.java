package com.jlpay.qrcode.external.business.dependency.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ChannelJsPayResponse extends ChannelBaseResponse {

    /**微信: 支付交易会话 ID,<br>
     * 银联二维码: 重定向地址,<br>
     * 支付宝: 银联交易号,唤起支付宝所需
     * */
    @JSONField(name = "pay_info")
    private String payInfo;
}
