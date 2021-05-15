package com.jlpay.qrcode.external.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

/**
 * @author qihuaiyuan
 * @since 2021/05/15
 */
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("config.buisness")
public class BusinessProperties {

    @NotNull
    private Duration defaultInvokeTimeout = Duration.ofSeconds(34);
}
