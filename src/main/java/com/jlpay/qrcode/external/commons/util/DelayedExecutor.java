package com.jlpay.qrcode.external.commons.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;

/**
 * @author qihuaiyuan
 * @since 2021/05/15
 */
@Slf4j
public class DelayedExecutor implements Executor, ShutdownAware {

    private final Executor workerExec;

    private final int queueSize;

    private final DelayQueue<DelayedTask> delayedTasks;

    private volatile boolean shutdown = false;

    public static DelayedExecutorBuilder builder() {
        return new DelayedExecutorBuilder();
    }

    public DelayedExecutor(Executor workerExec, int queueSize) {
        if (workerExec == null) {
            throw new NullPointerException();
        }
        if (queueSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.workerExec = workerExec;
        this.queueSize = queueSize;
        delayedTasks = new DelayQueue<>();
        startListenerThread();
    }

    private void startListenerThread() {
        Thread listenerThread = new ListenerThread();
        listenerThread.start();
    }

    public <T extends DelayedTask> void executeDelayedTask(T delayedTask) {
        int currentTasks = delayedTasks.size();
        if (currentTasks >= queueSize) {
            throw new RejectedExecutionException("延迟队列满: " + queueSize);
        }
        delayedTasks.add(delayedTask);
    }

    @Override
    public void execute(Runnable command) {
        if (shutdown) {
            throw new IllegalStateException("executor at shutdown state");
        }
        if (command instanceof DelayedTask) {
            executeDelayedTask((DelayedTask) command);
            return;
        }
        workerExec.execute(command);
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }

    private class ListenerThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    DelayedTask delayedTask = delayedTasks.take();
                    workerExec.execute(delayedTask.getTask());
                } catch (InterruptedException e) {
                    log.info("listener thread interrupted. Probably program shut down");
                    return;
                }
            }
        }
    }

    public static class DelayedExecutorBuilder {

        private Executor workerExec;

        private int queueSize = Integer.MAX_VALUE;

        private Consumer<RejectedExecutionException> rejectHandler = e -> {
            throw e;
        };

        public DelayedExecutorBuilder workerExec(Executor workerExec) {
            this.workerExec = workerExec;
            return this;
        }

        public DelayedExecutorBuilder queueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }

        public DelayedExecutorBuilder rejectHandler(Consumer<RejectedExecutionException> rejectHandler) {
            this.rejectHandler = rejectHandler;
            return this;
        }

        public DelayedExecutor build() {
            return new DelayedExecutor(workerExec, queueSize);
        }
    }
}
