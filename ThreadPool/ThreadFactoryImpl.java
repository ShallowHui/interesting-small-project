package test;

public class ThreadFactoryImpl implements ThreadFactory {

    public Thread newThread(Runnable r) {
        // 直接返回一个线程对象
        return new Thread(r);
    }

}