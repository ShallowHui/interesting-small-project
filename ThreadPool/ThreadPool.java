package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 编写线程池工具类
 */
public class ThreadPool implements Executor {

    private int corePoolSize; // 核心线程数（常驻内存）
    private int maxPoolSize; // 最大工作线程数（核心线程+非核心线程）
    private AtomicInteger workCount = new AtomicInteger(0); // 正在工作的线程数
    private RejectedExecutionHandler rejectedExecutionHandler; // 拒绝策略
    private TimeUnit timeUnit; // 时间单位
    private long keepAliveTime; // 非核心线程的空闲时间（非核心线程不常驻内存，空闲时间内未能从等待队列中获得任务则结束运行）

    private BlockingQueue<Runnable> blockingQueue; // 任务的等待阻塞队列
    private ThreadFactory threadFactory; // 线程工厂

    /**
     * 通过此方法给线程池提交任务
     * @param r 要执行的任务对象
     */
    public void execute(Runnable r) {
        if (workCount.get() < corePoolSize) {
            workCount.getAndIncrement(); // 工作线程数加一，要在线程创建运行前进行
            threadFactory.newCoreThread(r, blockingQueue).start();
        } else {
            if (!blockingQueue.offer(r)) {
                if (workCount.get() < maxPoolSize) {
                    workCount.getAndIncrement();
                    threadFactory.newNonCoreThread(r, blockingQueue, timeUnit, keepAliveTime, workCount).start();
                } else {
                    rejectedExecutionHandler.rejectedHandle(r);
                }
            }
        }
    }

    /**
     * 线程池的构造方法，可以配置线程池的各个参数
     */
    public ThreadPool(int corePoolSize, int maxPoolSize, RejectedExecutionHandler rejectedExecutionHandler,
                      TimeUnit timeUnit, long keepAliveTime, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        this.timeUnit = timeUnit;
        this.keepAliveTime = keepAliveTime;

        this.blockingQueue = blockingQueue;
        this.threadFactory = threadFactory;

        // 额外开启一个线程监听线程池
        new Thread(() -> {
            while (true) {
                System.out.println("当前等待队列中任务数：" + blockingQueue.size() + " <------> " + "当前工作线程数：" + workCount.get());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}