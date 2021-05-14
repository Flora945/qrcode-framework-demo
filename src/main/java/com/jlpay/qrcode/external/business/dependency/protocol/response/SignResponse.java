package com.jlpay.qrcode.external.business.dependency.protocol.response;

import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
@Setter
public class SignResponse extends DefaultResponse {

    private String content;
}
