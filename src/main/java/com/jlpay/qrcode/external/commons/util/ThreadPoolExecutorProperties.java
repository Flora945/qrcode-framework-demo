package com.jlpay.qrcode.external.commons.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * @author qihuaiyuan
 * @since 2021/05/15
 */
@Getter
@Setter
@Validated
public class ThreadPoolExecutorProperties {

    @Positive
    private int corePoolSize;

    @Positive
    private int maxPoolSize;

    @Positive
    private int queueCapacity;

    @NotBlank
    private String threadNamePrefix;

    @Positive
    private int keepAliveSeconds;

    public ThreadPoolTaskExecutor createThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);
        return executor;
    }
}
