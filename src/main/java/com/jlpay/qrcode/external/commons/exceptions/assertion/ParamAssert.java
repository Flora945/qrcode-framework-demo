package com.jlpay.qrcode.external.commons.exceptions.assertion;

import com.jlpay.qrcode.external.commons.exceptions.ExceptionCodes;
import org.apache.commons.lang3.StringUtils;

/**
 * @author qihuaiyuan
 * @since 2021/5/5
 */
public final class ParamAssert {

    private ParamAssert() {
        throw new UnsupportedOperationException("This class is not instantiable");
    }

    public static void isTrue(boolean expression, String msgPattern, Object... args) {
        BusiAssert.isTrue(expression, ExceptionCodes.BAD_REQUEST_PARAM, msgPattern, args);
    }

    public static void notBlank(String str, String msgPattern, Object... args) {
        isTrue(StringUtils.isNotBlank(str), msgPattern, args);
    }
}
