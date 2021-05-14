package com.jlpay.qrcode.external.business.protocol.request;

import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Data
@Slf4j
public class CancelTransRequest extends RefundTransRequest {

    @Override
    public void validate() {
        log.debug("效验请求参数格式");
        validateParamsLength(getPayType(),10);
        validateParamsLength(getOutTradeNo(),32);
        validateParamsLength(getOriOutTradeNo(),32);
        validateParamsLength(getTotalFee(),15);
        validateParamsLength(getRemark(),256);
        validateParamsLength(getLongitude(),20);
        validateParamsLength(getLatitude(),20);

        ParamAssert.notBlank(getMchId(), "商户号不能为空");
        ParamAssert.notBlank(getService(), "接口类型不能为空");
        ParamAssert.notBlank(getOrgCode(), "机构号不能为空");
        if (StringUtils.isEmpty(getSigned())){
            ParamAssert.notBlank(getSign(), "签名不能为空");
        }
        ParamAssert.notBlank(getNonceStr(), "随机字符串不能为空");

        ParamAssert.notBlank(getOutTradeNo(), "外部订单号不能为空");
        if (!ApiConstants.OLD_VERSION.equals(getQrcodeVersion())){
            ParamAssert.notBlank(getMchCreateIp(), "终端IP不能为空");
        }
    }
}
