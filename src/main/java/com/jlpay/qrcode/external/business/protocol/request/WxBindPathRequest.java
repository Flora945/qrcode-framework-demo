package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class WxBindPathRequest extends BaseTransRequest {

    /**JSAPI支付授权目录*/
    @JSONField(name = "jsapi_path")
    private String jsapiPath;

    @Override
    public void validate() {
        super.validate();
        validateParamsLength(jsapiPath,128);
        ParamAssert.isTrue(ApiConstants.WXPAY.equals(getPayType()), "请传入正确的pay_type字段");
        ParamAssert.isTrue(StringUtils.isNotBlank(jsapiPath)||StringUtils.isNotBlank(getSubAppid()),"sub_appid和jsapi_path,不能同时为空");
        if (!ApiConstants.OLD_VERSION.equals(getQrcodeVersion())){
            ParamAssert.notBlank(getMchCreateIp(), "终端IP不能为空");
        }
    }
}
