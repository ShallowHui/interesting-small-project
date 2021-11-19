package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 编写线程池工具类
 */
public class ThreadPool implements Executor {

    private final int corePoolSize; // 核心线程数（常驻内存）
    private final int maxPoolSize; // 最大工作线程数（核心线程+非核心线程）
    private final AtomicInteger workCount = new AtomicInteger(0); // 正在工作的线程数
    private final RejectedExecutionHandler rejectedExecutionHandler; // 拒绝策略
    private final TimeUnit timeUnit; // 时间单位
    private final long keepAliveTime; // 非核心线程的空闲时间（非核心线程不常驻内存，空闲时间内未能从等待队列中获得任务则结束运行）

    private final BlockingQueue<Runnable> blockingQueue; // 任务的等待阻塞队列
    private final ThreadFactory threadFactory; // 线程工厂

    /**
     * 通过此方法给线程池提交任务
     * @param r 要执行的任务对象
     */
    public void execute(Runnable r) {
        if (workCount.get() < corePoolSize) {
            // 创建核心线程
            Worker coreWorker = new Worker(r, true);
            coreWorker.thread.start();
        } else {
            // 如果任务进入等待队列失败, 判断是否可以创建非核心线程，不能创建则走拒绝策略
            if (!blockingQueue.offer(r)) {
                if (workCount.get() < maxPoolSize) {
                    Worker nonCoreWorker = new Worker(r, false);
                    nonCoreWorker.thread.start();
                } else {
                    rejectedExecutionHandler.rejectedHandle(this, r);
                }
            }
        }
    }

    public String toString() {
        return "可弹性伸缩的线程池";
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
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String time = df.format(new Date());
                System.out.println("当前等待队列中任务数：" + blockingQueue.size() + " <------> " + "当前工作线程数：" +
                        workCount.get() + "   时间：" + time);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 工作线程类，即线程池中具体运行的线程对象是Worker的实例
     */
    private final class Worker implements Runnable {

        private Runnable task; // 工作线程的任务
        private boolean core;

        public final Thread thread; // 以Worker实例对象自身通过ThreadFactory创建一个Thread，再保存到实例中

        /**
         * Worker的构造方法，可以判断要创建的是不是核心线程
         * @param firstTask 创建工作线程时，设置要运行的第一个任务
         * @param core 判断是否为核心线程
         */
        public Worker(Runnable firstTask, boolean core) {
            this.task = firstTask;
            this.core = core;
            this.thread = threadFactory.newThread(this);

            workCount.getAndIncrement(); // 工作线程数量加一
        }

        // 执行任务
        public void run() {
            if (core)
                runCoreWorker();
            else
                runNonCoreWorker();
        }

        // 核心线程常驻内存
        public void runCoreWorker() {
            System.out.println(Thread.currentThread().getName() + "核心线程已启动运行");
            while (true) {
                if (task != null)
                    task.run();
                task = blockingQueue.poll();
            }
        }

        // 非核心线程会结束运行
        public void runNonCoreWorker() {
            System.out.println(Thread.currentThread().getName() + "非核心线程已启动运行");
            while (task != null) {
                task.run();
                try {
                    task = blockingQueue.poll(keepAliveTime, timeUnit);
                } catch (InterruptedException e) {
                    task = null;
                    e.printStackTrace();
                }
            }
            workCount.getAndDecrement(); // 非核心线程结束运行，工作线程数量减一
            System.out.println(Thread.currentThread().getName() + "非核心线程已结束运行");
        }
    }

}