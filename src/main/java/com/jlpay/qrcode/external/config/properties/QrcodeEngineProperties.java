package com.jlpay.qrcode.external.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "jlpay.qrcode.engin")
public class QrcodeEngineProperties {
    private String queue;
    private int queryTimes;
    private long queryWaitTime;
    private String url;
    private long delayCancelTime;

    /**
     * 老版外接码付地址
     */
    private String legacyExtQrUrl;
}
