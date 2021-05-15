package com.jlpay.qrcode.external.config;

import com.jlpay.utils.XxTeaCrypto;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库相关配置
 *
 * @author qihuaiyuan
 * @since 2020-08-07
 */
@Configuration
public class DbConfig {

    private static final String PREFIX = "$ENC";

    /**
     * 属性提取器, 对加密的字段进行解密
     *
     * @return 可以对设定类型的加密属性字段进行解密的提取器
     */
    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
        return s -> {
            if (s.startsWith(PREFIX)) {
                return XxTeaCrypto.decrypt(s.substring(PREFIX.length()), "JLZF/POS+/DB/ENC/KEY/URI");
            }
            return s;
        };
    }
}
