package com.jlpay.qrcode.external.business.dependency;

import com.alibaba.fastjson.JSON;
import com.jlpay.qrcode.external.business.dependency.protocol.request.SignRequest;
import com.jlpay.qrcode.external.business.dependency.protocol.response.SignResponse;
import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;
import com.jlpay.qrcode.external.commons.exceptions.assertion.BusiAssert;
import com.jlpay.qrcode.external.commons.io.protocol.DefaultResponse;
import com.jlpay.qrcode.external.config.properties.SignServiceProperties;
import com.jlpay.qrcode.external.support.ApiConstants;
import com.jlpay.transport.client.thrift.SyncThriftService;
import com.jlpay.utils.exception.BaseException;
import com.jlpay.utils.exception.BusiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 签名服务
 *
 * @author lvlinyang
 * @since 2019/10/23 14:32
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SignService {

    private final SyncThriftService syncThriftService;

    private final SignServiceProperties properties;

    public String doSign(String content) throws BusiException {
        SignRequest request = createRequest(content);
        request.setCommandId(SignRequest.SIGN_COMMAND);

        String responseStr = syncThriftService.invoke(properties.getRequestPath(),
                JSON.toJSONString(request), (int) properties.getRequestTimeout().toMillis());

        SignResponse response = JSON.parseObject(responseStr, SignResponse.class);
        BusiAssert.isTrue(response.isSuccess(), ExceptionCodes.SYSTEM_EXCEPTION, "加签失败");
        return response.getContent();
    }

    public boolean verifySign(String context) throws BaseException {
        SignRequest request = createRequest(context);
        request.setCommandId(SignRequest.VERIFY_SIGN_COMMAND);

        String responseStr = syncThriftService.invoke(properties.getRequestPath(),
                JSON.toJSONString(request), (int) properties.getRequestTimeout().toMillis());

        DefaultResponse response = JSON.parseObject(responseStr, DefaultResponse.class);
        return response.isSuccess();
    }

    private SignRequest createRequest(String content) {
        SignRequest request = new SignRequest();
        request.setUrlType(ApiConstants.BUSI_TYPE_8001);
        request.setContext(content);
        return request;
    }
}

