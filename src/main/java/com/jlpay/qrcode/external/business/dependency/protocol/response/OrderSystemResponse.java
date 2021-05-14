package com.jlpay.qrcode.external.business.dependency.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import lombok.Data;

/**
 * 新订单系统响应
 *
 * @author lvlinyang
 * @date 2021/3/19 15:30
 */
@Data
public class OrderSystemResponse extends DefaultResponse {

    /**
     * 订单号
     */
    @JSONField(name = "order_id")
    private String orderId;
    /**
     * 交易时间，银联二维码、微信、支付宝类交易填写渠道的支付之间，银行卡填写当前时间
     */
    @JSONField(name = "trans_time")
    private String transTime;
    /**
     * 交易时间(yyyy-MM-dd HH:mm:ss)
     */
    @JSONField(name = "create_time")
    private String createTime;
}
