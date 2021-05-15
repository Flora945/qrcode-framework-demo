package com.jlpay.qrcode.external.commons.util;

import java.time.Duration;
import java.util.concurrent.Delayed;

/**
 * @author qihuaiyuan
 * @since 2021/05/15
 */
public interface DelayedTask extends Delayed {

    Runnable getTask();

    static DelayedTask create(Duration delay, Runnable task) {
        return new DefaultDelayedTask(task, delay.toMillis());
    }

    static DelayedTask create(long delayMillis, Runnable task) {
        return new DefaultDelayedTask(task, delayMillis);
    }

}
