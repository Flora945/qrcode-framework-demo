package com.jlpay.qrcode.external.commons.exceptions.assertion;

import com.jlpay.qrcode.external.commons.exceptions.def.BusiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;


/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public final class BusiAssert {

    private BusiAssert() {
        throw new UnsupportedOperationException("This class is not instantiable");
    }

    /**
     * if the value of param <code>expression</code> is false, then throw a BusiException using the given code and message
     *
     * @param expression the boolean value to choose whether throw the exception or not
     * @param retCode    exception return code
     * @param msgPattern message pattern using the SLF4J-Style
     * @param args       the message arguments
     */
    public static void isTrue(boolean expression, String retCode, String msgPattern, Object... args) {
        if (!expression) {
            throw new BusiException(retCode, MessageFormatter.arrayFormat(msgPattern, args).getMessage());
        }
    }

    public static void notNull(Object obj, String retCode, String msgPattern, Object... args) {
        isTrue(obj != null, retCode, msgPattern, args);
    }

    public static void notBlank(String str, String retCode, String msgPattern, Object... args) {
        isTrue(StringUtils.isNotBlank(str), retCode, msgPattern, args);
    }

}
