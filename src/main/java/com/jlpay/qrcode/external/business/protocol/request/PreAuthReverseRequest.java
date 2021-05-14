package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

/**
 * 预授权反向交易请求(关单/撤销)
 *
 * @author qihuaiyuan
 * @date 2020/1/6 15:06
 */
@Data
public class PreAuthReverseRequest extends CancelTransRequest {

    @JSONField(name = "busi_type")
    private String busiType = ApiConstants.BUSI_TYPE_8001;

    @JSONField(name = "busi_sub_type")
    private String busiSubType;

    @JSONField(name = "trans_address")
    private String transAddress;

    @JSONField(name = "area_code")
    private String areaCode;

    @JSONField(name = "guarantee_auth_code")
    private String guaranteeAuthCode;

    @Override
    public void validate() {
        validateParamsLength(getOutTradeNo(),32);
        validateParamsLength(getOriOutTradeNo(),32);
        validateParamsLength(getTotalFee(),15);
        validateParamsLength(getRemark(),256);
        validateParamsLength(getLongitude(),20);
        validateParamsLength(getLatitude(),20);

        ParamAssert.notBlank(getPayType(), "交易类型(pay_type)不能为空");
        ParamAssert.notBlank(guaranteeAuthCode, "担保交易授权码不能为空");
        ParamAssert.notBlank(getMchId(), "商户号不能为空");
        ParamAssert.notBlank(getService(), "接口类型不能为空");
        ParamAssert.notBlank(getOrgCode(), "机构号不能为空");
        if (StringUtils.isEmpty(getSigned())){
            ParamAssert.notBlank(getSign(), "签名不能为空");
        }
        ParamAssert.notBlank(getNonceStr(), "随机字符串不能为空");
        ParamAssert.notBlank(getOutTradeNo(), "外部订单号不能为空");
        ParamAssert.notBlank(getMchCreateIp(), "终端IP不能为空");
        ParamAssert.notBlank(getOriOutTradeNo(), "原外部订单号不能为空");
        ParamAssert.notBlank(getTotalFee(), "金额不能为空");
        ParamAssert.isTrue(Pattern.matches("^[1-9]\\d*$", getTotalFee()), "金额格式不对");
    }
}
