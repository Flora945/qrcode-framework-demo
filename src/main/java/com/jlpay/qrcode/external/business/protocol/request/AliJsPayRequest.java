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
public class AliJsPayRequest extends BaseTransRequest {

    /**业务大类*/
    private String busiType = ApiConstants.BUSI_TYPE_8001;
    /**业务小类*/
    private String busiSubType = BusiSubType.JSPAY.getFlag();
    /**交易类型*/
    private String tradeType = TradeType.OFFACCOUNTS.getValue();

    /**买家支付宝账号*/
    @JSONField(name = "buyer_logon_id")
    private String buyerLogonId;
    /**买家支付宝用户ID*/
    @JSONField(name = "buyer_id")
    private String buyerId;

    @Override
    public void validate() {
        super.validate();
        validateParamsLength(buyerLogonId,32);
        validateParamsLength(buyerId,32);
        ParamAssert.isTrue(PayType.alipay.getPayType().equals(getPayType()), "请传入正确的pay_type字段");
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
        ParamAssert.isTrue(StringUtils.isNotBlank(buyerLogonId) || StringUtils.isNotBlank(buyerId), "买家支付宝账号和买家支付宝用户ID不能同时为空");
        if (PayType.alipay.getPayType().equals(getPayType()) && "1".equals(getIsHirePurchase())) {
            ParamAssert.notBlank(getHirePurchaseNum(), "分期数不能为空");
            ParamAssert.isTrue(Pattern.matches("^(3|6|12)$", getHirePurchaseNum()), "分期数格式不对");
            ParamAssert.notBlank(getHirePurchaseSellerPercent(), "卖家承担的手续费比例不能为空");
            ParamAssert.isTrue(Pattern.matches("^([1-9]\\d?|100|0)$", getHirePurchaseSellerPercent()), "手续费比例格式不对");
            busiSubType = BusiSubType.FC_FWC_PAY.getFlag();
        }
    }
}
