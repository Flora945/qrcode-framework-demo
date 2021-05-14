package com.jlpay.qrcode.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author qihuaiyuan
 * @since 2021/05/04
 */
@EnableScheduling
@SpringBootApplication
public class ExtQrcodeApiRefactorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtQrcodeApiRefactorApplication.class, args);
    }

}

