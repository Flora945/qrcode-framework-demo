package com.jlpay.qrcode.external.business.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseTransResponse {
    /**返回码*/
    @JSONField(name = "ret_code")
    private String retCode = "00";
    /**返回信息*/
    @JSONField(name = "ret_msg")
    private String retMsg = "成功";
    /**签名*/
    @JSONField(name = "sign")
    private String sign;

    /**机构号*/
    @JSONField(name = "org_code")
    private String orgCode;

    /**订单状态*/
    private String status;
    /**商户号*/
    @JSONField(name = "mch_id")
    private String mchId;
    /**嘉联订单号*/
    @JSONField(name = "transaction_id")
    private String transactionId;
    /**商户订单号*/
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**总金额 是 String 总金额，以分为单位，不允许包含任何字、符号*/
    @JSONField(name = "total_fee")
    private String totalFee;
    /**商户订单时间，yyyy-MM-dd HH:mm:ss*/
    @JSONField(name = "order_time",format = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;
    /**订单支付时间，yyyy-MM-dd HH:mm:ss*/
    @JSONField(name = "trans_time",format = "yyyy-MM-dd HH:mm:ss")
    private Date transTime;

    /**支付渠道*/
    @JSONField(name = "pay_type")
    private String payType;

    /**
     * 渠道订单号
     */
    @JSONField(name = "chn_transaction_id")
    private String chnTransactionId;

    /**二维码状态:<br/>
     * 1:已扫码
     * */
    @JSONField(name = "qrcode_state")
    private String qrcodeState;

}
