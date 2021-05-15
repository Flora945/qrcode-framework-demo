package com.jlpay.qrcode.external.business.process.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jlpay.qrcode.external.business.services.MerchInfoService;
import com.jlpay.qrcode.external.business.services.SignService;
import com.jlpay.qrcode.external.business.services.protocol.response.LMerchInfoResponse;
import com.jlpay.qrcode.external.business.protocol.enums.TransType;
import com.jlpay.qrcode.external.business.protocol.request.BaseTransRequest;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;
import com.jlpay.qrcode.external.commons.exceptions.assertion.BusiAssert;
import com.jlpay.qrcode.external.commons.exceptions.assertion.ParamAssert;
import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import com.jlpay.qrcode.external.controller.BusiProcessContext;
import com.jlpay.qrcode.external.db.model.OutQrMerchantKey;
import com.jlpay.qrcode.external.db.service.OutQrMerchantKeyService;
import com.jlpay.qrcode.external.support.ApiConstants;
import com.jlpay.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

/**
 * 外接码付业务处理
 *
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Slf4j
public abstract class AbstractExtQrcodeApiBusiProcess<R extends BaseTransRequest> extends AbstractBusinessProcess<R> {

    private SignService signService;

    private OutQrMerchantKeyService merchantKeyService;

    private MerchInfoService merchInfoService;

    private boolean needSignature;

    private String orgCode;

    private String mchId;

    protected LMerchInfoResponse lMerchInfo;

    @Value("#{${jlpay.commons.system.merch.service-type}}")
    private Map<String, String> serviceTypeMap;

    @Override
    protected void handleBusiness() {
        checkSign();
        R request = getRequest();
        request.setService(getProcessContext().getRequestName());
        // 校验请求参数
        request.validate();
        // 校验机构/商户/终端等
        validateMerch();
        // 处理具体业务
        handleExtQrcodeBusiness();
    }

    private void validateMerch() {
        R request = getRequest();
        log.debug("查询机构权限配置");
        OutQrMerchantKey merchKey = merchantKeyService.selectByPrimaryKey(request.getOrgCode());
        boolean outQrMerchantKeyFlag = merchKey != null;
        // V1.5版接口不校验终端状态
        boolean termFlag = outQrMerchantKeyFlag
                && ApiConstants.CHECK_FIELD_TRUE.equals(merchKey.getIfCheckTerm())
                && TransType.ifCheckService(request.getService())
                && !ApiConstants.OLD_VERSION.equals(request.getQrcodeVersion());

        if (outQrMerchantKeyFlag) {
            // 校验机构状态与接口权限
            ParamAssert.isTrue(ApiConstants.CHECK_FIELD_TRUE.equals(merchKey.getStatus()), "请检查机构状态");
            ParamAssert.isTrue(checkService(merchKey), "未拥有此接口权限");
        }

        log.debug("查询商户信息");
        // 根据条件选择查询商户终端信息或商户信息
        lMerchInfo = termFlag ? merchInfoService.queryLMerchTermInfo(request.getMchId(), request.getTermNo())
                : merchInfoService.queryLMerchInfo(request.getMchId());
        // 校验商户状态
        ParamAssert.isTrue(lMerchInfo != null, "商户信息查询异常");
        ParamAssert.isTrue(ApiConstants.SUCCESS_CODE.equals(lMerchInfo.getRetCode()),
                lMerchInfo.getRetMsg() != null ? lMerchInfo.getRetMsg() : "商户信息查询异常");
        ParamAssert.isTrue(ApiConstants.CHECK_FIELD_TRUE.equals(lMerchInfo.getMerchState()), "请检查商户状态");
        if (termFlag) {
            // 校验终端状态
            ParamAssert.isTrue(ApiConstants.CHECK_FIELD_TRUE.equals(lMerchInfo.getTermState()), "终端不可用");
        }

        // 校验商户归属
        if (outQrMerchantKeyFlag && ApiConstants.CHECK_FIELD_TRUE.equals(merchKey.getIfCheckMerch())) {
            ParamAssert.isTrue(checkMerchBelong(merchKey), "非法的商户归属");
        }

        // 校验业务开通
        ParamAssert.isTrue(checkBusi(), "商户未开通业务[LMQ.NOT.MAP.CONFING]");
        request.setLMerchInfo(lMerchInfo);
    }

    /**
     * 检查业务开通
     *
     * @return
     */
    private boolean checkBusi() {
        R request = getRequest();
        // 绑定支付目录不校验码付套餐是否开通
        if (ApiConstants.AUTHBIND_SERVICE.equals(request.getService())) {
            return true;
        }
        String serviceType = serviceTypeMap.get(request.getService());
        ParamAssert.notBlank(serviceType, "未找到该交易对应的业务类型[LMQ.NOT.MAP.CONFING]");
        List<Map<String, String>> tradeTypeList = lMerchInfo.getTradeTypeList();
        for (Map<String, String> job : tradeTypeList) {
            if (serviceType.equals(job.get("trade_type"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验商户归属
     *
     * @return
     */
    private boolean checkMerchBelong(OutQrMerchantKey merchantKey) {
        R request = getRequest();
        if (ApiConstants.CHECK_FIELD_TRUE.equals(merchantKey.getAccessType())) {
            return merchInfoService.checkMerchAgentBelong(request.getOrgCode(), request.getMchId());
        }
        // V1.5版不校验厂商和商户接入归属
        if (ApiConstants.CHECK_FIELD_FALSE.equals(merchantKey.getAccessType()) && !ApiConstants.OLD_VERSION.equals(request.getQrcodeVersion())) {
            return merchInfoService.checkMerchGroupBelong(request.getOrgCode(), request.getMchId());
        }
        return true;
    }

    /**
     * 检查接口权限
     *
     * @return
     */
    private boolean checkService(OutQrMerchantKey merchantKey) {
        R request = getRequest();
        if (!TransType.ifCheckService(request.getService())) {
            return true;
        }
        String payType = request.getPayType();
        TransType transType = TransType.findByPayTypeAndServiceName(payType, request.getService());
        String ifaceType = transType.getValue();
        BusiAssert.notBlank(ifaceType, ExceptionCodes.BAD_REQUEST_PARAM, "未知的接口类型");
        String[] transArr = merchantKey.getTransType().split(",");
        for (String tp : transArr) {
            if (ifaceType.equals(tp)) {
                return true;
            }
        }
        return false;
    }

    private void checkSign() {
        BusiProcessContext processContext = getProcessContext();
        boolean signChecked = processContext.getParsedRequest().containsKey("signed");
        if (signChecked) {
            return;
        }
        log.debug("对请求报文验签");
        JSONObject requestJson = processContext.getParsedRequest();

        orgCode = requestJson.getString(ApiConstants.ORG_CODE);
        mchId = requestJson.getString(ApiConstants.MCH_ID);

        //验签通过,则继续加签,若验签失败,则不需加签,直接返回
        needSignature = signService.verifySign(processContext.getRawRequest());
        BusiAssert.isTrue(needSignature, ApiConstants.SIGN_NOT_ACCESS_RET_CODE, ApiConstants.SIGN_NOT_ACCESS_RET_MSG);
    }

    @Override
    protected void completeResponse(Object response) {
        if (!needSignature) {
            super.completeResponse(response);
            return;
        }
        log.debug("直接应答接入方,对应答报文加签");

        JSONObject json = (JSONObject) JSON.toJSON(response);
        json.put(ApiConstants.ORG_CODE, orgCode);
        json.put(ApiConstants.MCH_ID, mchId);

        String finalResponse;
        try {
            finalResponse = signService.doSign(json.toJSONString());
        } catch (Exception e) {
            log.error("加签异常", e);
            DefaultResponse errorResp = DefaultResponse.fromCodeAndMsg(Constants.SYSTEM_ERROR_CODE, Constants.SYSTEM_ERROR_MSG);
            super.completeResponse(errorResp);
            return;
        }
        super.completeResponse(finalResponse);
    }

    protected abstract void handleExtQrcodeBusiness();

    @Autowired
    public void setSignService(SignService signService) {
        this.signService = signService;
    }

    @Autowired
    public void setMerchantKeyService(OutQrMerchantKeyService merchantKeyService) {
        this.merchantKeyService = merchantKeyService;
    }

    @Autowired
    public void setMerchInfoService(MerchInfoService merchInfoService) {
        this.merchInfoService = merchInfoService;
    }
}
