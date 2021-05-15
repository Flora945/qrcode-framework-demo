package com.jlpay.qrcode.external.business.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jlpay.qrcode.external.business.services.protocol.response.LMerchInfoResponse;
import com.jlpay.qrcode.external.business.services.protocol.response.MerchBelongResponse;
import com.jlpay.qrcode.external.support.ApiConstants;
import com.jlpay.transport.client.thrift.SyncThriftService;
import com.jlpay.utils.exception.BusiAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author lvlinyang
 * @author lujianyuan
 * @since 2019/10/11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MerchInfoService {

    private final SyncThriftService syncThriftService;

    @Value("${jlpay.commons.system.merch.register-path}")
    private String merchRegisterNode;

    @Value("${jlpay.commons.system.merch.commandId.queryLMerchInfo}")
    private String queryLMerchInfo;

    @Value("${jlpay.commons.system.merch.timeout}")
    private int merchRequestTimeout;

    public LMerchInfoResponse queryLMerchInfo(String merchNo) {
        log.debug("查询左端商户信息,merchNo: {}", merchNo);
        LMerchInfoResponse lMerchInfoResponse;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command_id", queryLMerchInfo);
        jsonObject.put("merch_no", merchNo);
        String stringMerchInfo = syncThriftService.invoke(merchRegisterNode, jsonObject.toJSONString(), merchRequestTimeout);

        lMerchInfoResponse = JSON.parseObject(stringMerchInfo, LMerchInfoResponse.class);
        return lMerchInfoResponse;
    }

    public LMerchInfoResponse queryLMerchTermInfo(String merchNo, String termNo) {
        BusiAssert.isTrue(StringUtils.isNotEmpty(termNo), "终端号不能为空");
        log.debug("查询左端商户信息,merchNo: {}, termNo:{}", merchNo, termNo);

        LMerchInfoResponse lMerchInfoResponse;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command_id", "queryMerchTermInfoRpc");
        jsonObject.put("merch_no", merchNo);
        jsonObject.put("term_no", termNo);

        String strMerchInfo = syncThriftService.invoke(merchRegisterNode, jsonObject.toJSONString(), merchRequestTimeout);
        lMerchInfoResponse = JSONObject.parseObject(strMerchInfo, LMerchInfoResponse.class);
        return lMerchInfoResponse;
    }

    /**
     * 校验商户代理商归属
     *
     * @param agentId 代理商号
     * @param merchNo 商户号
     * @return
     */
    public boolean checkMerchAgentBelong(String agentId, String merchNo) {
        log.debug("校验商户代理商归属, agentId: {}, merchNo: {}", agentId, merchNo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command_id", "checkMerchAgentRpc");
        jsonObject.put("agent_id", agentId);
        jsonObject.put("merch_no", merchNo);

        try {
            String response = syncThriftService.invoke(merchRegisterNode, jsonObject.toJSONString(), merchRequestTimeout);
            MerchBelongResponse merchBelong = JSONObject.parseObject(response, MerchBelongResponse.class);
            if (merchBelong != null && ApiConstants.SUCCESS_CODE.equals(merchBelong.getRetCode()) && ApiConstants.CHECK_FIELD_FALSE.equals(merchBelong.getCheckResult())) {
                return false;
            }
        } catch (Exception e) {
            log.error("查询代理商归属异常", e);
        }
        return true;
    }

    /**
     * 校验商户集团归属
     *
     * @param groupId 集团id
     * @param merchNo 商户号
     * @return
     */
    public boolean checkMerchGroupBelong(String groupId, String merchNo) {
        log.debug("查询商户集团归属, groupId: {}, merchNo: {}", groupId, merchNo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command_id", "checkMerchGroupRpc");
        jsonObject.put("group_id", groupId);
        jsonObject.put("merch_no", merchNo);

        try {
            String response = syncThriftService.invoke(merchRegisterNode, jsonObject.toJSONString(), merchRequestTimeout);
            MerchBelongResponse merchBelong = JSONObject.parseObject(response, MerchBelongResponse.class);
            if (merchBelong != null && ApiConstants.SUCCESS_CODE.equals(merchBelong.getRetCode()) && ApiConstants.CHECK_FIELD_FALSE.equals(merchBelong.getCheckResult())) {
                return false;
            }
        } catch (Exception e) {
            log.error("查询商户集团归属异常", e);
        }
        return true;
    }
}
