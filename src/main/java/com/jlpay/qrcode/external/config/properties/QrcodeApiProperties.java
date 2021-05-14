package com.jlpay.qrcode.external.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lujianyuan
 * @since 2019/10/12
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jlpay.qrcode.api")
public class QrcodeApiProperties {
    private Map<String,String> refund;
    private Map<String,String> cancel;
    private int queryAfterMillis = 2000;
    private int cancelAfterMillis = 2000;
    private String payValidTime;

    public String getRefundBusiSubType(String transBusiSubType){
        return refund.get(transBusiSubType);
    }

    public String getCancelBusiSubType(String transBusiSubType){
        return cancel.get(transBusiSubType);
    }

}
