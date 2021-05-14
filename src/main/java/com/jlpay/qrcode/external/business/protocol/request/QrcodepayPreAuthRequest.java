package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.business.protocol.enums.PayType;
import com.jlpay.qrcode.external.business.protocol.enums.TradeType;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import com.jlpay.qrcode.external.support.PatternUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 主扫担保交易请求
 */
@Data
public class QrcodepayPreAuthRequest extends BaseTransRequest {

    /**
     * 业务大类
     */
    private String busiType = ApiConstants.BUSI_TYPE_8001;
    /**
     * 业务小类
     */
    private String busiSubType = BusiSubType.C2B_NATIVEPAY_PRE_AUTHORIZATION.getFlag();
    /**
     * 交易类型
     */
    private String tradeType = TradeType.QRCODE.getValue();

    /**
     * 担保有效时间，单位为天
     **/
    @JSONField(name = "guarantee_expiry")
    private String guaranteeExpiry;

    /**
     * 交易地址
     */
    @JSONField(name = "trans_address")
    private String transAddress;

    /**
     * 交易地区码
     */
    @JSONField(name = "area_code")
    private String areaCode;

    @JSONField(serialize = false)
    private boolean isNative;

    @Override
    public void validate() {
        super.validate();
        ParamAssert.notBlank(getPayType(), "交易类型(pay_type)不能为空");
        ParamAssert.notBlank(getOutTradeNo(), "外部订单号不能为空");
        ParamAssert.notBlank(getTotalFee(), "交易金额不能为空");
        ParamAssert.isTrue(PatternUtils.matches("^[1-9]\\d*$", getTotalFee()), "交易金额格式不对");
        ParamAssert.notBlank(getBody(), "商品标题不能为空");
        if (StringUtils.isNotBlank(getTermNo())) {
            ParamAssert.isTrue(PatternUtils.matches("^[a-zA-Z0-9]{1,8}$", getTermNo()), "终端设备号格式不对");
        }
        if (StringUtils.isNotBlank(getDeviceInfo())) {
            ParamAssert.isTrue(PatternUtils.matches("^[a-zA-Z0-9]{1,32}$", getDeviceInfo()), "终端设备号格式不对");
        }
        if (StringUtils.isNotBlank(getProfitSharing())) {
            ParamAssert.isTrue(PatternUtils.matches("^(0|1)$", getProfitSharing()), "分账格式不对");
        }
        if (StringUtils.isNotBlank(getRemark())) {
            ParamAssert.isTrue(getRemark().length() <= 500, "描述格式不对");
        }
        if (!ApiConstants.OLD_VERSION.equals(getQrcodeVersion())) {
            ParamAssert.notBlank(getMchCreateIp(), "终端IP不能为空");
        }
        ParamAssert.notBlank(getAttach(), "商品描述不能为空");
        if (PayType.unionpay.getPayType().equals(getPayType())) {
            ParamAssert.isTrue(getTermNoOrDeviceInfo().length() == 8, "终端号格式错误");
        }

        if (PayType.alipay.getPayType().equals(getPayType()) && "1".equals(getIsHirePurchase())) {
            ParamAssert.notBlank(getHirePurchaseNum(), "分期数不能为空");
            ParamAssert.isTrue(PatternUtils.matches("^(3|6|12)$", getHirePurchaseNum()), "分期数格式不对");
            ParamAssert.notBlank(getHirePurchaseSellerPercent(), "卖家承担的手续费比例不能为空");
            ParamAssert.isTrue(PatternUtils.matches("^([1-9]\\d?|100|0)$", getHirePurchaseSellerPercent()), "手续费比例格式不对");
            busiSubType = BusiSubType.FC_C2B_PAY.getFlag();
        }

        isNative = PayType.jlpay.getPayType().equals(getPayType()) || PayType.wxpay.getPayType().equals(getPayType());
        if (isNative) {
            tradeType = TradeType.NATIVE_PACKAGE.getValue();
        }
    }
}
