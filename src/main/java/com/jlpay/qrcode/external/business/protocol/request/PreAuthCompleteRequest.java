package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;

import java.util.regex.Pattern;

/**
 * @author qihuaiyuan
 * @date 2019/12/24 19:52
 */
@Data
public class PreAuthCompleteRequest extends BaseTransRequest {

    private String busiType = ApiConstants.BUSI_TYPE_8001;

    private String busiSubType = BusiSubType.PRE_AUTHORIZATION_COMPLETE.getFlag();

    @JSONField(name = "guarantee_auth_code")
    private String guaranteeAuthCode;

    @JSONField(name = "trans_address")
    private String transAddress;

    @JSONField(name = "area_code")
    private String areaCode;

    @Override
    public void validate() {
        super.validate();
        ParamAssert.notBlank(getPayType(), "交易类型(pay_type)不能为空");
        ParamAssert.notBlank(guaranteeAuthCode, "担保交易授权码不能为空");
        ParamAssert.notBlank(getOutTradeNo(), "外部订单号不能为空");
        ParamAssert.notBlank(getOriOutTradeNo(), "原外部订单号不能为空");
        ParamAssert.notBlank(getTotalFee(), "金额不能为空");
        ParamAssert.isTrue(Pattern.matches("^[1-9]\\d*$", getTotalFee()), "预授权完成金额格式不对");
    }

}
