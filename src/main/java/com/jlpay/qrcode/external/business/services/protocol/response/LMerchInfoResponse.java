package com.jlpay.qrcode.external.business.services.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LMerchInfoResponse {
    @JSONField(name = "agent_id")
    public String agentId;
    @JSONField(name = "mcc_code")
    public String mccCode;
    @JSONField(name = "merch_name")
    public String merchName;
    @JSONField(name = "merch_no")
    public String merchNo;
    @JSONField(name = "merch_state")
    public String merchState;
    @JSONField(name = "net_type")
    public String netType;
    @JSONField(name = "print_merch_name")
    public String printMerchName;
    @JSONField(name = "print_merch_no")
    public String printMerchNo;
    @JSONField(name = "region_code")
    public String regionCode;
    @JSONField(name = "ret_code")
    public String retCode;
    @JSONField(name = "ret_msg")
    public String retMsg;
    @JSONField(name = "risk_type")
    public String riskType;
    @JSONField(name = "settle_type")
    public String settleType;
    @JSONField(name = "trade_type_list")
    public List<Map<String, String>> tradeTypeList;


    @JSONField(name = "term_no")
    public String termNo;
    @JSONField(name = "device_no")
    public String deviceNo;
    @JSONField(name = "term_state")
    public String termState;

    /**
     * 商户参数:  key为字段名,value为逗号分割的限制类型
     * wechat_forbidden_card_type : 0.没有禁用  1.借记卡 2.贷记卡
     * alipay_forbidden_card _type : 0.没有禁用  1.借记卡 2.贷记卡
     * */
    @JSONField(name = "merch_param")
    public Map<String, String> merchParam;
}
