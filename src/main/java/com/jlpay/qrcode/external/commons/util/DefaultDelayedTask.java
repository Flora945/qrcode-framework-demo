package com.jlpay.qrcode.external.commons.util;

import com.jlpay.utils.Constants;
import org.slf4j.MDC;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author qihuaiyuan
 * @since 2021/05/15
 */
public class DefaultDelayedTask implements DelayedTask {

    private final Runnable task;

    private final long endTimestamp;

    public DefaultDelayedTask(Runnable task, long delayMillis) {
        String logId = MDC.get(Constants.LOGID);
        this.task = () -> {
            MDC.put(Constants.LOGID, logId);
            try {
                task.run();
            } finally {
                MDC.remove(Constants.LOGID);
            }
        };
        endTimestamp = System.currentTimeMillis() + delayMillis;
    }

    @Override
    public Runnable getTask() {
        return task;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(endTimestamp - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }
}
