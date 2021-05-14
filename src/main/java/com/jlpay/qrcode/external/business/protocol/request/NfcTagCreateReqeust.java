package com.jlpay.qrcode.external.business.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;

@Data
public class NfcTagCreateReqeust extends StaticCodeReqeust {

    /**业务大类*/
    private String busiType = ApiConstants.BUSI_TYPE_1001;
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
        super.validate();

    }
}
