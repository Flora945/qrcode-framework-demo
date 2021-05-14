package com.jlpay.qrcode.external.config;

import brave.ScopedSpan;
import brave.Tracing;
import com.jlpay.qrcode.external.commons.Constants;
import com.jlpay.qrcode.external.commons.events.EventLevel;
import com.jlpay.qrcode.external.commons.events.EventReporter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author qihuaiyuan
 * @since 2021/5/4
 */
@Getter
@Setter
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties("config.executors")
public class ExecutorConfig {

    private final Tracing tracing;

    private final EventReporter eventReporter;

    private ThreadPoolTaskExecutor business;

    @Bean
    public Executor business() {
        business.initialize();
        return enhance(business);
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
}
