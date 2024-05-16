package com.wxz.train.batch.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * TestJob类实现了Job接口，用于定义一个定时任务。
 * 该任务被标记为不允许并发执行，即同一时间只能有一个实例在执行。
 */
@DisallowConcurrentExecution
public class TestJob implements Job {

    /**
     * execute方法是定时任务的具体逻辑执行点。
     * @param jobExecutionContext 提供了执行上下文，包括作业详情和触发器信息等。
     * @throws JobExecutionException 如果作业执行中发生异常，则抛出此异常。
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 打印任务开始信息
        System.out.println("TestJob TEST开始");
        try {
            // 模拟任务执行过程，暂停3秒
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // 打印线程中断异常
            e.printStackTrace();
        }
        // 打印任务结束信息
        System.out.println("TestJob TEST结束");
    }
}
