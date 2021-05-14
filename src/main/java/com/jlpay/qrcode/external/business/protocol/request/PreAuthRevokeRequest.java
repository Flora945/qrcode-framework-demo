package com.jlpay.qrcode.external.business.protocol.request;

import com.jlpay.qrcode.external.business.protocol.enums.BusiSubType;
import com.jlpay.qrcode.external.support.ApiConstants;
import lombok.Data;

/**
 * @author qihuaiyuan
 * @date 2019/12/24 19:52
 */
@Data
public class PreAuthRevokeRequest extends CancelTransRequest {

    private String busiType = ApiConstants.BUSI_TYPE_8001;

    private String busiSubType = BusiSubType.PRE_AUTHORIZATION_CANCEL.getFlag();

}
