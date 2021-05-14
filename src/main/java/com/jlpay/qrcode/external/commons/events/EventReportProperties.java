package com.jlpay.qrcode.external.commons.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("config.event-report")
public class EventReportProperties {

    @NotBlank
    private String requestUrl;

    @NotBlank
    private String model;

    @NotBlank
    private String serviceName;

    @NotNull
    private Duration requestTimeout = Duration.ofSeconds(3);

    private boolean dropDuplicates = true;

    @NotNull
    private Duration dropDuplicatesIn = Duration.ofMinutes(2);
}
