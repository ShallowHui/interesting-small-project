package test;

public class RejectedImpl implements RejectedExecutionHandler {

    public void rejectedHandle(Executor executor, Runnable r) {
        // 直接输出信息
        System.out.println("Task: " + r.toString() + " rejected from: " + executor.toString());
    }
}