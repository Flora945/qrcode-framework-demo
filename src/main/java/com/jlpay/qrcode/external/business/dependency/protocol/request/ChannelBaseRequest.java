package com.jlpay.qrcode.external.business.dependency.protocol.request;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.dependency.protocol.response.LMerchInfoResponse;
import lombok.Data;

import java.util.Date;

@Data
public class ChannelBaseRequest {

    @JSONField(name = "command_id")
    private String commandId;
    /**支付通道 alipay:支付宝，wxpay:微信，unionpay:银联，qqpay:QQ钱包支付*/
    @JSONField(name = "pay_type")
    private String payType;
    /**订单号*/
    @JSONField(name = "order_id")
    private String orderId;
    /**金额:单位:分*/
    private String amount;
    /**嘉联商户号*/
    @JSONField(name = "merch_no")
    private String merchNo;
    /**终端号*/
    @JSONField(name = "term_no")
    private String termNo;
    /**业务大类*/
    @JSONField(name = "busi_type")
    private String busiType;
    /**业务小类*/
    @JSONField(name = "busi_sub_type")
    private String busiSubType;

    @JSONField(name = "trans_time",format = "yyyy-MM-dd HH:mm:ss")
    private Date transTime;

    /**机构号*/
    @JSONField(name = "org_code")
    private String orgCode;

    /**
     * 限制信用卡支付
     */
    @JSONField(name = "limit_pay")
    private String limitPay;

    /**来源*/
    private String source;

    /**二维码类型 0-动态 1-静态*/
    @JSONField(name = "code_type")
    private String codeType;

    /**
     * 备注
     */
    private String remark;

    /**左端商户信息*/
    @JSONField(name = "lmerch_info")
    private LMerchInfoResponse LMerchInfo;

    /**渠道请求参数*/
    @JSONField(name = "channel_request_params")
    private JSONObject channelRequestParams;
}
