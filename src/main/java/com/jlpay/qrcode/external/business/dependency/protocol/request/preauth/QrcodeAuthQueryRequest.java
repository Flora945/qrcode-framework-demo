package com.jlpay.qrcode.external.business.dependency.protocol.request.preauth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 订单查询请求接口协议
 *
 * Created by wangyuhai1 on 2019/10/31.
 */
@Data
public class QrcodeAuthQueryRequest {

    public static final String COMMAND_ID = "QrcodeAuthQuery";

    @JSONField(name = "command_id")
    private String commandId = COMMAND_ID;

    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "busi_type")
    private String busiType;

    @JSONField(name = "busi_sub_type")
    private String busiSubType;

    @JSONField(name = "pay_type")
    private String payType;

}
