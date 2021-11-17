package test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryImpl implements ThreadFactory {

    public Thread newCoreThread(Runnable r, BlockingQueue<Runnable> blockingQueue) {
        return new Thread(){

            Runnable temp = r;

            public void run() {
                System.out.println(Thread.currentThread().getName() + "核心线程已创建");
                while (true) {
                    if (temp != null)
                        temp.run();
                    temp = blockingQueue.poll();
                }
            }
        };
    }

    public Thread newNonCoreThread(Runnable r, BlockingQueue<Runnable> blockingQueue, TimeUnit timeUnit,
                                   long keepAliveTime, AtomicInteger workCount) {
        return new Thread() {

            Runnable temp = r;

            public void run() {
                System.out.println(Thread.currentThread().getName() + "非核心线程已创建");
                while (temp != null) {
                    temp.run();
                    try {
                        temp = blockingQueue.poll(keepAliveTime, timeUnit);
                    } catch (InterruptedException e) {
                        temp = null;
                        e.printStackTrace();
                    }
                }
                workCount.getAndDecrement();
                System.out.println(Thread.currentThread().getName() + "非核心线程已结束运行");
            }
        };
    }
}