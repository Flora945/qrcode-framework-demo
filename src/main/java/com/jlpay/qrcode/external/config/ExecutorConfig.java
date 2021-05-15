package com.jlpay.qrcode.external.config;

import brave.ScopedSpan;
import brave.Tracing;
import com.jlpay.qrcode.external.commons.Constants;
import com.jlpay.qrcode.external.commons.events.EventLevel;
import com.jlpay.qrcode.external.commons.events.EventReporter;
import com.jlpay.qrcode.external.commons.util.DelayedExecutor;
import com.jlpay.qrcode.external.commons.util.ThreadPoolExecutorProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.concurrent.Executor;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ExecutorConfig {

    private final Tracing tracing;

    private final EventReporter eventReporter;

    @Bean
    @Validated
    @ConfigurationProperties("config.executors.business")
    public ThreadPoolExecutorProperties businessExecutorProperties() {
        return new ThreadPoolExecutorProperties();
    }

    @Bean
    @Validated
    @ConfigurationProperties("config.executors.delayed-executor")
    public DelayedExecutorProperties delayedExecutorProperties() {
        return new DelayedExecutorProperties();
    }

    @Bean
    public Executor business(@Qualifier("businessExecutorProperties") ThreadPoolExecutorProperties properties) {
        ThreadPoolTaskExecutor executor = properties.createThreadPoolTaskExecutor();
        // todo reject handler
        executor.initialize();
        return enhance(executor);
    }

    @Bean
    public DelayedExecutor delayedExecutor(@Qualifier("delayedExecutorProperties") DelayedExecutorProperties properties) {
        ThreadPoolTaskExecutor executor = properties.getExecutor().createThreadPoolTaskExecutor();
        executor.initialize();
        return DelayedExecutor.builder()
                .workerExec(executor)
                .queueSize(properties.getQueueSize())
                .build();
    }

    /**
     * 对Executor进行增强，加入span/logId追踪，以及异常告警功能
     *
     * @param delegate 被代理的Executor
     * @return 增强后的executor
     */
    private Executor enhance(Executor delegate) {
        return task -> {
            String logId = MDC.get(Constants.MDC_LOG_ID_KEY);
            delegate.execute(() -> {
                MDC.put(Constants.MDC_LOG_ID_KEY, logId);
                ScopedSpan span = tracing.tracer().startScopedSpanWithParent("http-response", tracing.currentTraceContext().get());
                try {
                    task.run();
                } catch (Exception e) {
                    log.error("FATAL Task execution exception NOT handled", e);
                    eventReporter.prepareReport()
                            .level(EventLevel.NORMAL)
                            .cause(e)
                            .requestName("BUSI_EXEC_EXPOSED")
                            .send();
                } finally {
                    MDC.remove(Constants.MDC_LOG_ID_KEY);
                    span.finish();
                }
            });
        };
    }

    @Getter
    @Setter
    @Validated
    public static class DelayedExecutorProperties {

        @Positive
        private int queueSize;

        @NotNull
        private ThreadPoolExecutorProperties executor;
    }
}
