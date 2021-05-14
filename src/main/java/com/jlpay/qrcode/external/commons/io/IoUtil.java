package com.jlpay.qrcode.external.commons.io;

import com.jlpay.qrcode.external.commons.exceptions.def.NetworkException;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public final class IoUtil {

    private IoUtil() {
        throw new UnsupportedOperationException("This class is not instantiable");
    }

    public static byte[] getRequestBody(ServletRequest request) {
        try {
            return StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new NetworkException("网络异常", "请求读取失败: " + e.getLocalizedMessage(), e);
        }
    }

    public static void writeResponse(byte[] data, ServletResponse response) {
        try {
            StreamUtils.copy(data, response.getOutputStream());
        } catch (IOException e) {
            throw new NetworkException("网络异常", "响应写入失败" + e.getLocalizedMessage(), e);
        }
    }
}
