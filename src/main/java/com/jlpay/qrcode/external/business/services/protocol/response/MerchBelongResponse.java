package com.jlpay.qrcode.external.business.services.protocol.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description 商户归属校验返回类
 * @Author zhaoyang
 * @Date 2019/10/25 15:00
 **/
public class MerchBelongResponse {

    @JSONField(name = "ret_code")
    public String retCode;
    @JSONField(name = "ret_msg")
    public String retMsg;
    /**
     * 校验结果(1:校验通过，0:校验不通过)
     */
    @JSONField(name = "check_result")
    public String checkResult;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }
}
