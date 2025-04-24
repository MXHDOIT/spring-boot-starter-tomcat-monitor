package com.frank.tomcat.starter.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author: maxinhang.
 */
public class TomcatExecutorMonitoringDecorator implements Executor {
    private static final Logger logger = LoggerFactory.getLogger(TomcatExecutorMonitoringDecorator.class);

    private final Executor delegate;

    public TomcatExecutorMonitoringDecorator(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(Runnable command) {
        long submitTime = System.currentTimeMillis();
        delegate.execute(() -> {
            long startTime = System.currentTimeMillis();
            long queueTime = startTime - submitTime;

            // 记录队列等待时间（单位：毫秒）
            recordQueueTime(queueTime);

            // 执行原始任务
            command.run();
        });
    }

    private void recordQueueTime(long queueTime) {
        logger.info("tomcat.thread.queue.wait.time {}", queueTime);
    }

    public Executor getExecutor() {
        return delegate;
    }
}
