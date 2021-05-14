package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.protocol.enums.PayType;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class OpenIdRequest extends BaseTransRequest {

    /**授权码*/
    @JSONField(name = "auth_code")
    private String authCode;

    /**银联支付标识*/
    @JSONField(name = "app_up_identifier")
    private String appUpIdentifier;

    @Override
    public void validate() {
        super.validate();
        validateParamsLength(authCode,50);
        ParamAssert.isTrue(StringUtils.isNotBlank(PayType.findPayType(getPayType()).getPayType()), "请传入正确的pay_type字段");
        ParamAssert.isTrue(!ApiConstants.ALIPAY.equals(getPayType()), "暂不支持此交易类型");
        ParamAssert.notBlank(authCode, "授权码不能为空");
    }
}
