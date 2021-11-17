package test;

import java.lang.reflect.Field;

public class RejectedImpl implements RejectedExecutionHandler {

    public void rejectedHandle(Runnable r) {
        try {
            // 通过反射获取任务对象的成员属性
            Field field = r.getClass().getField("x");
            System.out.println("休眠" + field.get(r) + "秒的任务提交失败！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}