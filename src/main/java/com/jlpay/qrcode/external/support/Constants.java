package com.jlpay.qrcode.external.support;

/**
 * @author lujianyuan
 * @date 2019-07-28
 * 会话级别的常量
 */
public final class Constants {
    private Constants(){}

    public static final String LOGID="logId";
    /**应答码标签*/
    public static final String RET_CODE="ret_code";
    /**应答信息标签*/
    public static final String RET_MSG="ret_msg";

    /**响应成功应答码*/
    public static final String SUCCESS_CODE = "00";

    /**系统异常代码*/
    public static final String SYSTEM_ERROR_CODE="96";
    /**
     * 系统繁忙
     */
    public static final String SYSTEM_BUSY = "系统繁忙，请稍后重试";
    /**系统等待超时*/
    public static final String SYSTEM_TIME_OUT="98";
    public static final String BUSI_ERROR_CODE="ZZ";
    /**系统异常应答信息*/
    public static final String SYSTEM_ERROR_MSG="系统异常";
    /**系统等待超时信息*/
    public static final String SYSTEM_TIME_OUT_MSG="交易超时,请重试";

    /**交易查询频率限制*/
    public static final String ORDER_QUERY_FREQUENCY_LIMIT = "查询频率过高，请稍后再试";
    /**交易撤销频率限制*/
    public static final String ORDER_CANCEL_FREQUENCY_LIMIT = "撤销频率过高，请稍后再试";

    /**Zookeeper删除。。。。**/
    public static final char ZK_NODE_DELIMITER = '/';
    /**Zookeeper后缀**/
    public static final String ZK_NODE_SUFFIX = "/";

}
