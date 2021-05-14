package com.jlpay.qrcode.external.commons.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
@AllArgsConstructor
public enum EventLevel {

    /**
     * 提示
     */
    HINT("1"),
    /**
     * 一般
     */
    NORMAL("2"),
    /**
     * 严重
     */
    FATAL("3"),
    /**
     * 紧急
     */
    EMERGENCY("4"),
    ;

    private final String value;

}
