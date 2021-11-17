package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// 用户自定义线程的创建
public interface ThreadFactory {
    /**
     * 创建核心线程
     */
    public Thread newCoreThread(Runnable r, BlockingQueue<Runnable> blockingQueue);

    /**
     * 创建非核心线程
     */
    public Thread newNonCoreThread(Runnable r, BlockingQueue<Runnable> blockingQueue, TimeUnit timeUnit,
                                   long keepAliveTime, AtomicInteger workCount);
}