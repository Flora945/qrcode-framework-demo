package com.jlpay.qrcode.external.business.dependency.protocol.request;

import com.jlpay.qrcode.external.business.dependency.protocol.request.BaseRpcRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
@Setter
public class SignRequest extends BaseRpcRequest {

    public static final String SIGN_COMMAND = "sign";

    public static final String VERIFY_SIGN_COMMAND = "verifySign";

    private String urlType;

    private String context;

}
