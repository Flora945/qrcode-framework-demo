package com.jlpay.qrcode.external.support;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lujianyuan
 * @since 2019/11/7
 */
public class PatternUtils {

    private static Map<String,Pattern> PATTERNMAP = new HashMap<>();
    public static boolean matches(String regex, CharSequence input) {
        Pattern p = PATTERNMAP.get(regex);
        if(p==null){
            p = Pattern.compile(regex);
            PATTERNMAP.put(regex,p);
        }
        Matcher m = p.matcher(input);
        return m.matches();
    }
}
