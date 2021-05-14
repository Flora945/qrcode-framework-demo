package com.jlpay.qrcode.external.commons.io.protocol;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.jlpay.qrcode.external.commons.Constants;
import lombok.Setter;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Setter
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class DefaultResponse implements Response {

    private String retCode;

    private String retMsg;

    public static DefaultResponse fromCodeAndMsg(String code, String msg) {
        DefaultResponse response = new DefaultResponse();
        response.setRetCode(code);
        response.setRetMsg(msg);
        return response;
    }

    @Override
    public String getRetCode() {
        return retCode;
    }

    @Override
    public String getRetMsg() {
        return retMsg;
    }

    @Override
    public boolean isSuccess() {
        return Constants.RET_CODE_SUCCESS.equals(retCode);
    }
}
