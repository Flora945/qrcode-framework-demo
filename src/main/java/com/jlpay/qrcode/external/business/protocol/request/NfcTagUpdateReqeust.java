package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class NfcTagUpdateReqeust extends StaticCodeReqeust {

    /**业务大类*/
    private String busiType = ApiConstants.BUSI_TYPE_8001;
    /**业务小类*/
    private String busiSubType = BusiSubType.NFC_PAY.getFlag();

    /**标签ID*/
    @JSONField(name="nfc_tag_id")
    private String nfcTagId;

    /**旧的标签ID*/
    @JSONField(name = "old_nfc_tag_id")
    private String oldNfcTagId;

    @Override
    public void validate() {
        ParamAssert.notBlank(getOrgCode(), "机构号不能为空");
        ParamAssert.notBlank(getMchId(), "商户号不能为空");
        ParamAssert.notBlank(getTermNo() , "终端号不能为空");
        ParamAssert.isTrue(StringUtils.isNotBlank(nfcTagId),"标签ID不能为空");
        ParamAssert.isTrue(StringUtils.isNotBlank(oldNfcTagId),"旧标签ID不能为空");
    }
}
