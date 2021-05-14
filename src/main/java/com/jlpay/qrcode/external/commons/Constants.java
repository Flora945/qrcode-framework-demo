package com.jlpay.qrcode.external.commons;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("This class is not instantiable");
    }

    public static final String MDC_LOG_ID_KEY = "logId";

    public static final String RET_CODE_SUCCESS = "00";
    public static final String RET_MESSAGE_SUCCESS = "处理成功";
    public static final String DUPLICATED_WARNING_MESSAGE = "重复告警";
    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_WITHOUT_TIME = "yyyy-MM-dd";
    public static final String TIME_FORMAT_WITHOUT_DATE = "HH:mm:ss";
    public static final String DATE_FORMAT_WITHOUT_TIME_CONDENSED = "yyyyMMdd";

}
