package com.jlpay.qrcode.external.commons.exceptions.assertion;

import com.jlpay.qrcode.external.commons.exceptions.def.SystemException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public final class Assert {

    private Assert() {
        throw new UnsupportedOperationException("Not instantiable");
    }

    public static void isTrue(boolean expression, String expCode, String msg, String eventMessage) {
        if (!expression) {
            throw new SystemException(expCode, msg, eventMessage);
        }
    }

}
