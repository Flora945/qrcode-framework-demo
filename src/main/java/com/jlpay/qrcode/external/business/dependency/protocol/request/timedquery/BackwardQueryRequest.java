package com.jlpay.qrcode.external.business.dependency.protocol.request.timedquery;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 交易反查请求
 *
 * @author qihuaiyuan
 * @since 2020-09-22
 */
@Getter
@Setter
public class BackwardQueryRequest {

    @JSONField(name = "orderId")
    private String orderId;
}
