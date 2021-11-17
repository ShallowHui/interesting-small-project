package test;

// 处理线程池拒绝
public interface RejectedExecutionHandler {
    /**
     * 任务提交失败时执行的方法
     * @param r 任务对象
     */
    public void rejectedHandle(Runnable r);
}