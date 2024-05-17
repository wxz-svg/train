package com.wxz.train.batch.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.wxz.train.batch.feign.BusinessFeign;
import com.wxz.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Date;

/**
 * 日程车次任务类，实现了Quartz的Job接口，用于执行每日车次数据的生成任务。
 * 通过@DisallowConcurrentExecution注解确保该任务不会并发执行。
 */
@DisallowConcurrentExecution
public class DailyTrainJob implements Job {

    // 日志记录器
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainJob.class);

    @Resource
    BusinessFeign businessFeign;

    /**
     * 任务执行方法。当Quartz调度器触发该任务时，会执行此方法。
     *
     * @param jobExecutionContext 包含任务执行上下文的信息，例如触发时间等。
     * @throws JobExecutionException 如果任务执行中发生异常。
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 初始化日志标识符，用于追踪和日志记录
        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));
        LOG.info("生成15天后的车次数据开始");

        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, 15);
        Date offsetDate = dateTime.toJdkDate();
        CommonResp<Object> commonResp = businessFeign.genDaily(offsetDate);
        LOG.info("生成15天后的车次数据结束,结果：{}", commonResp);
    }
}
