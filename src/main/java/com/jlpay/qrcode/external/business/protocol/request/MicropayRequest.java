package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.business.protocol.enums.TradeType;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

@Data
public class MicropayRequest extends BaseTransRequest {

    /**业务大类*/
    private String busiType = ApiConstants.BUSI_TYPE_8001;
    /**业务小类*/
    private String busiSubType = BusiSubType.B2C_MICROPAY.getFlag();
    /**交易类型*/
    private String tradeType = TradeType.AUTHCODE.getValue();

    /**授权码*/
    @JSONField(name = "auth_code")
    private String authCode;

    /**false:异步请求<br>
     * true:同步被扫,得到最终支付结果
     * */
    @JSONField(name = "sync_able")
    private boolean syncAble;

    @Override
    public void validate() {
        super.validate();
        //授权码格式判断
        ParamAssert.notBlank(authCode, "授权码不能为空");
        validateParamsLength(authCode,50);
        if (Pattern.matches("^1[0-5]\\d{16}$", authCode)) {
            setPayType(ApiConstants.WXPAY);
        } else if (Pattern.matches("(^2[5-9]\\d{14,22}$)|(^30\\d{14,22}$)", authCode)) {
            setPayType(ApiConstants.ALIPAY);
        } else if (Pattern.matches("^62\\d*$", authCode)) {
            setPayType(ApiConstants.UNIONPAY);
        } else if (Pattern.matches("^91\\d*$", authCode)) {
            setPayType(ApiConstants.QQPAY);
        } else {
            ParamAssert.isTrue(false, "不支持该类型的授权码");
        }

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
        if (ApiConstants.UNIONPAY.equals(getPayType())) {
            ParamAssert.isTrue(getTermNoOrDeviceInfo().length() == 8, "终端号格式错误");
        }
        if (ApiConstants.ALIPAY.equals(getPayType()) && "1".equals(getIsHirePurchase())) {
            ParamAssert.notBlank(getHirePurchaseNum(), "分期数不能为空");
            ParamAssert.isTrue(Pattern.matches("^(3|6|12)$", getHirePurchaseNum()), "分期数格式不对");
            ParamAssert.notBlank(getHirePurchaseSellerPercent(), "手续费比例不能为空");
            ParamAssert.isTrue(Pattern.matches("^([1-9]\\d?|100|0)$", getHirePurchaseSellerPercent()), "手续费比例格式不对");
            busiSubType = BusiSubType.FC_B2C_PAY.getFlag();
        }
    }
}
