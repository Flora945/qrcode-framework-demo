package com.jlpay.qrcode.external.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("config.dependency.sign-service")
public class SignServiceProperties {

    @NotBlank
    private String requestPath;

    @NotNull
    private Duration requestTimeout = Duration.ofSeconds(3);

}
