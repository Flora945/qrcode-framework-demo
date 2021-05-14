package com.jlpay.qrcode.external.config.properties;

import com.jlpay.utils.Constants;
import com.jlpay.utils.exception.BusiAssert;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;

/**
 * 订单系统配置
 *
 * @author lvlinyang
 * @date 2021/3/18 18:10
 */
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("jlpay.commons.system.order-system")
public class OrderSystemProperties {

    /**
     * 请求url前缀
     */
    @NotBlank
    private String urlPrefix;

    /**
     * 请求读取超时时间
     */
    private int readTimeout = 3000;

}
