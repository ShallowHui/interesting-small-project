package test;

// 线程工厂类，用户可以自定义线程的创建
public interface ThreadFactory {
    public Thread newThread(Runnable r);
}