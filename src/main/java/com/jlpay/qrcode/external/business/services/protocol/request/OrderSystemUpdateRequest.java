package com.jlpay.qrcode.external.business.services.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Description: 订单系统请求
 * @author: lvlinyang
 * @date: 2021/3/19 15:30
 */
@Data
public class OrderSystemUpdateRequest {

    /**订单号*/
    @JSONField(name = "order_id")
    private String orderId;
    /**授权码*/
    @JSONField(name = "auth_code")
    private String authCode;
    /**订单状态(0-初始 1-待确认 2-成功 3-失败 默认: 0)*/
    private String status;
    /**手续费手续费，正常扣除手续费使用填写使用+号， 退回手续费请使用-号*/
    private String fee;
    /**计费类型*/
    @JSONField(name = "fee_type")
    private String feeType;
    /**响应码*/
    @JSONField(name = "ret_code")
    private String retCode;
    /**响应信息*/
    @JSONField(name = "ret_msg")
    private String retMsg;
    /**支付方式，微信支付-wxpay;支付宝-alipay;银联二维码-unionpay;银行卡-bankpay*/
    @JSONField(name = "pay_type")
    private String payType;
    /**交易时间，银联二维码、微信、支付宝类交易填写渠道的支付之间，银行卡填写当前时间*/
    @JSONField(name = "trans_time")
    private String transTime;
    /**支付凭证：提交支付所使用的tocken,如果是是用微信支付宝，银联二维码，则是使用的auth_code,如果是使用的是刷卡支付，则填写脱敏后的银行卡号*/
    @JSONField(name = "pay_token")
    private String payToken;

}
