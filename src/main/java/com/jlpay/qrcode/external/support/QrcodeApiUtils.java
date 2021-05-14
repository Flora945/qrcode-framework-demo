package com.jlpay.qrcode.external.support;

import org.apache.commons.lang.StringUtils;

/**
 * @Description: 工具类
 * @author: lvlinyang
 * @date: 2019/10/19 14:39
 */
public class QrcodeApiUtils {

    /**
     * 字符串金额转为Long类型
     * @param str 字符串
     */
    public static Long parseStrToLong(String str){
        if (StringUtils.isNotEmpty(str)){
            return Long.parseLong(str);
        }
        return null;
    }

    /**
     * Long类型金额转换为字符串
     * @param amount 金额
     */
    public static String parseLongToStr(Long amount){
        if (amount!=null){
            return amount.toString();
        }
        return null;
    }
}
