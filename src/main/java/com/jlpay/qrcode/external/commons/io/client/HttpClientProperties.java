package com.jlpay.qrcode.external.commons.io.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;

/**
 * HTTP客户端配置
 *
 * @author qihuaiyuan
 * @since 2021/02/05
 */
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("config.http-client")
public class HttpClientProperties {

    /**
     * 最大请求超时时间
     */
    @NotNull
    private Duration maxRequestTimeout = Duration.ofSeconds(60);

    /**
     * 连接超时时间
     */
    @Positive
    private Integer connectTimeoutMillis = 1000;
}
