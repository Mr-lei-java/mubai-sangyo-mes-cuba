package com.company.sangyo.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.*;

/**
 * 执行器工厂
 *
 * @author liumin
 */
public class ThreadPoolExecutors {

    private static ScheduledExecutorService scheduledExecutor;

    private static ExecutorService asyncExecutor;

    private static EventLoopGroup nioEventLoopGroup;

    public synchronized static ScheduledExecutorService getScheduledExecutor() {
        if (scheduledExecutor == null) {
            ThreadFactory scheduledThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("scada-scheduled-pool-%d").build();
            int processors = Runtime.getRuntime().availableProcessors();
            scheduledExecutor
                    = new ScheduledThreadPoolExecutor(processors + 1, scheduledThreadFactory);
        }
        return scheduledExecutor;
    }

    public synchronized static ExecutorService getAsyncExecutor() {
        if (asyncExecutor == null) {
            ThreadFactory executorThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("scada-executor-pool-%d").build();
            int processors = Runtime.getRuntime().availableProcessors();
            asyncExecutor = new ThreadPoolExecutor(
                    2 * processors + 1,
                    200,
                    5000L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(1024),
                    executorThreadFactory,
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
        return asyncExecutor;
    }

    public synchronized static EventLoopGroup getNioEventLoopGroup() {
        if (nioEventLoopGroup == null) {
            nioEventLoopGroup = new NioEventLoopGroup();
        }
        return nioEventLoopGroup;
    }
}
