package com.jlpay.qrcode.external.business.dependency.protocol.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
@Setter
public class BaseRpcRequest {

    @JSONField(name = "command_id")
    private String commandId;
}
