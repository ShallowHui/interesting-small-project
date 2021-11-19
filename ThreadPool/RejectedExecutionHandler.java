package test;

// 处理线程池拒绝提交任务的策略
public interface RejectedExecutionHandler {
    /**
     * 任务提交失败时执行的方法
     * @param executor Executor的实现类
     * @param r 任务对象
     */
    public void rejectedHandle(Executor executor, Runnable r);
}