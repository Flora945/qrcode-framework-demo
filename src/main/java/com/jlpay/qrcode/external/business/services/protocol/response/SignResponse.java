package com.jlpay.qrcode.external.business.services.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
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

    @JSONField(name = "context")
    private String content;
}
