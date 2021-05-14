package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;

@Data
public class StaticCodeReqeust extends BaseTransRequest {

    /**业务大类*/
    private String busiType = ApiConstants.BUSI_TYPE_1001;
    /**业务小类*/
    private String busiSubType = BusiSubType.STATIC_CODE.getFlag();

    /**商户名*/
    @JSONField(name="merch_name")
    private String merchName;

    @Override
    public void validate() {
        ParamAssert.notBlank(getMchId(), "商户号不能为空");
        ParamAssert.notBlank(getService(), "接口类型不能为空");
        ParamAssert.notBlank(getOrgCode(), "机构号不能为空");
        ParamAssert.notBlank(getTermNo(), "终端号不能为空");
        ParamAssert.notBlank(getOutTradeNo(), "外部订单号不能为空");

        ParamAssert.notBlank(getBody(), "商品标题不能为空");
        ParamAssert.notBlank(getDeviceInfo(), "设备号不能为空");
        if (!ApiConstants.OLD_VERSION.equals(getQrcodeVersion())){
            ParamAssert.notBlank(getMchCreateIp(), "终端IP不能为空");
        }
    }
}
