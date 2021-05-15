package com.jlpay.qrcode.external.business.services.protocol.request.preauth;

import com.alibaba.fastjson.annotation.JSONField;
import com.jlpay.utils.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * 担保请求协议基类
 *
 * Created by wangyuhai1 on 2019/10/31.
 */
@Data
public class QrcodeAuthBaseRequest {
    /**命令字**/
    @JSONField(name = "command_id")
    private String commandId;
    /**业务小类**/
    @JSONField(name = "busi_sub_type")
    private String busiSubType;
    /**业务大类**/
    @JSONField(name = "busi_type")
    private String busiType;
    /**交易类型：wxpay、alipay、unionpay**/
    @JSONField(name = "pay_type")
    private String payType;
    /**订单号**/
    @JSONField(name = "order_id")
    private String orderId;
    /**交易金额**/
    @JSONField(name = "amount")
    private String amount;
    /**商户号**/
    @JSONField(name = "merch_no")
    private String merchNo;
    /**商户名称**/
    @JSONField(name = "merch_name")
    private String merchName;
    /**终端号：posp交易必填**/
    @JSONField(name = "term_no")
    private String termNo;
    /**机身号：posp交易必填**/
    @JSONField(name = "term_sn")
    private String termSn;
    /**订单标题**/
    @JSONField(name = "subject")
    private String subject;
    /**订单详细信息**/
    @JSONField(name = "subject_detail")
    private String subjectDetail;
    /**交易时间 yyyy-MM-dd HH:mm:ss**/
    @JSONField(name = "trans_time", format = DateUtil.NEW_FORMAT)
    private Date transTime;
    /**交易IP**/
    @JSONField(name = "trans_ip")
    private String transIp;
    /**交易地址**/
    @JSONField(name = "trans_address")
    private String transAddress;
    /**交易地区码**/
    @JSONField(name = "trans_area_code")
    private String transAreaCode;
    /**位置信息：经纬度、基站**/
    @JSONField(name = "position_info")
    private String positionInfo;

}
