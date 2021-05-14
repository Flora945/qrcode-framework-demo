package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class OrderQueryRequest extends BaseTransRequest {

    /**嘉联订单号*/
    @JSONField(name = "transaction_id")
    private String transactionId;

    @Override
    public void validate() {
        super.validate();
        ParamAssert.isTrue(StringUtils.isNotBlank(transactionId)||StringUtils.isNotBlank(getOutTradeNo()),"嘉联订单号和商户订单号不能同时为空");
    }
}
