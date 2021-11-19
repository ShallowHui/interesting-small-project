package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    /**
     * 编写一个Runnable接口的实现类，run方法内定义了要执行的任务
     */
    static class Task implements Runnable {

        public long x;

        public Task(long x) {
            this.x = x;
        }

        public String toString() {
            return "休眠" + x + "秒";
        }

        public void run() {
            try {
                TimeUnit.SECONDS.sleep(x);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("========================================================================>休眠" +
                    x + "秒的任务已由" + Thread.currentThread().getName() + "线程完成");
        }
    }

    public static void main(String[] args) throws Exception {
        RejectedExecutionHandler rejectedExecutionHandler = new RejectedImpl();
        ThreadFactory threadFactory = new ThreadFactoryImpl();
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(9);

        ThreadPool threadPool = new ThreadPool(3, 6, rejectedExecutionHandler,
                TimeUnit.MILLISECONDS, 100L, blockingQueue, threadFactory);

        threadPool.execute(new Task(30L));

        TimeUnit.SECONDS.sleep(3);

        threadPool.execute(new Task(20L));
        threadPool.execute(new Task(10L));
        threadPool.execute(new Task(40L));
        threadPool.execute(new Task(35L));
        threadPool.execute(new Task(60L));
        threadPool.execute(new Task(15L));
        threadPool.execute(new Task(25L));
        threadPool.execute(new Task(50L));

        threadPool.execute(new Task(61L));
        threadPool.execute(new Task(62L));
        threadPool.execute(new Task(63L));
        threadPool.execute(new Task(64L));
        threadPool.execute(new Task(65L));
        threadPool.execute(new Task(86L));
        threadPool.execute(new Task(67L));
        threadPool.execute(new Task(68L));
        threadPool.execute(new Task(69L));

        TimeUnit.MINUTES.sleep(2);

        threadPool.execute(new Task(90));
        threadPool.execute(new Task(91));
        threadPool.execute(new Task(92));
        threadPool.execute(new Task(93));
        threadPool.execute(new Task(94));
        threadPool.execute(new Task(95));
        threadPool.execute(new Task(96));
        threadPool.execute(new Task(97));
        threadPool.execute(new Task(98));
        threadPool.execute(new Task(99));
        threadPool.execute(new Task(100));
        threadPool.execute(new Task(101));
        threadPool.execute(new Task(102));
        threadPool.execute(new Task(103));
        threadPool.execute(new Task(104));
        threadPool.execute(new Task(105));
        threadPool.execute(new Task(106));
    }
}