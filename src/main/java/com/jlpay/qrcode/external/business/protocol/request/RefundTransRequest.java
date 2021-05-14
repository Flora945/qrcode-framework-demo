package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class RefundTransRequest extends BaseTransRequest {

    /**业务大类*/
    private String busiType = ApiConstants.BUSI_TYPE_8001;

    /**异步请求,还是同步请求<br>
     * 同步退款,则得到最终退款结果
     * */
    @JSONField(name = "sync_able")
    private boolean syncAble;

    @Override
    public void validate() {
        super.validate();
        validateParamsLength(getOriOutTradeNo(),32);
        validateParamsLength(getOriTransactionId(),42);
        ParamAssert.notBlank(getOutTradeNo(), "外部订单号不能为空");
        //兼容老外接码付的撤销,不送交易金额,要取原订单金额
        if (!ApiConstants.OLD_VERSION.equals(getQrcodeVersion())){
            ParamAssert.notBlank(getTotalFee(), "交易金额不能为空");
            ParamAssert.isTrue(Pattern.matches("^[1-9]\\d*$", getTotalFee()), "交易金额格式不对");
        }
        if (!ApiConstants.OLD_VERSION.equals(getQrcodeVersion())){
            ParamAssert.notBlank(getMchCreateIp(), "终端IP不能为空");
        }
    }
}
