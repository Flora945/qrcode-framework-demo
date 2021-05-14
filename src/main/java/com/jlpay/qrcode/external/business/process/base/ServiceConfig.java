package com.jlpay.qrcode.external.business.process.base;

import java.lang.annotation.*;

/**
 * @author qihuaiyuan
 * @since 2021/05/04
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceConfig {

    Class<?> requestType() default String.class;

}
