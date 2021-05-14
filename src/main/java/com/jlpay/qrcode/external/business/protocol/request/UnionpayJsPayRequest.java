package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.business.protocol.enums.PayType;
import com.jlpay.qrcode.external.business.protocol.enums.TradeType;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

@Data
public class UnionpayJsPayRequest extends BaseTransRequest {

    /**业务大类*/
    private String busiType = ApiConstants.BUSI_TYPE_8001;
    /**业务小类*/
    private String busiSubType = BusiSubType.JSPAY.getFlag();
    /**交易类型*/
    private String tradeType = TradeType.OFFACCOUNTS.getValue();

    /**用户授权码（机构和银联交互获得）*/
    @JSONField(name = "user_auth_code")
    private String userAuthCode;
    /**银联支付标识（机构和银联交互获得）*/
    @JSONField(name = "app_up_identifier")
    private String appUpIdentifier;

    /**银联用户信息*/
    @JSONField(name = "user_id")
    private String userId;

    @Override
    public void validate() {
        super.validate();
        validateParamsLength(userAuthCode,32);
        validateParamsLength(appUpIdentifier,32);
        ParamAssert.notBlank(getPayType(), "交易类型不能为空");
        ParamAssert.isTrue(PayType.unionpay.getPayType().equals(getPayType()), "请传入正确的pay_type字段");
        ParamAssert.notBlank(getOutTradeNo(), "外部订单号不能为空");
        ParamAssert.notBlank(getTotalFee(), "交易金额不能为空");
        ParamAssert.isTrue(Pattern.matches("^[1-9]\\d*$", getTotalFee()), "交易金额格式不对");
        ParamAssert.notBlank(getBody(), "商品标题不能为空");
        if (StringUtils.isNotBlank(getTermNo())) {
            ParamAssert.isTrue(Pattern.matches("^[a-zA-Z0-9]{1,8}$", getTermNo()), "终端设备号格式不对");
        }
        if (StringUtils.isNotBlank(getDeviceInfo())) {
            ParamAssert.isTrue(Pattern.matches("^[a-zA-Z0-9]{1,32}$", getDeviceInfo()), "终端设备号格式不对");
        }
        if (StringUtils.isNotBlank(getProfitSharing())) {
            ParamAssert.isTrue(Pattern.matches("^(0|1)$", getProfitSharing()), "分账格式不对");
        }
        if (StringUtils.isNotBlank(getRemark())) {
            ParamAssert.isTrue(getRemark().length() <= 500, "描述格式不对");
        }
        if (!ApiConstants.OLD_VERSION.equals(getQrcodeVersion())){
            ParamAssert.notBlank(getMchCreateIp(), "终端IP不能为空");
        }
        ParamAssert.notBlank(getAttach(), "商品描述不能为空");

        boolean flag = StringUtils.isNotBlank(userAuthCode)&&StringUtils.isNotBlank(appUpIdentifier);
        ParamAssert.isTrue(flag||StringUtils.isNotBlank(userId),"用户授权信息不能为空");
    }
}
