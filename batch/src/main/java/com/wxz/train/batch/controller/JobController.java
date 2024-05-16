package com.wxz.train.batch.controller;

import com.wxz.train.batch.req.CronJobReq;
import com.wxz.train.batch.resp.CronJobResp;
import com.wxz.train.common.resp.CommonResp;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/admin/job")
public class JobController {

    private static Logger LOG = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 添加一个定时任务。
     *
     * @param cronJobReq 包含定时任务配置信息的对象，如任务类名、分组、CRON表达式和描述。
     * @return CommonResp 包含操作结果的信息，如是否成功和失败消息。
     */
    @RequestMapping(value = "/add")
    public CommonResp add(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        LOG.info("创建定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);
        CommonResp commonResp = new CommonResp();

        try {
            // 初始化调度器
            Scheduler sched = schedulerFactoryBean.getScheduler();

            // 启动调度器
            sched.start();

            // 构建Job详情
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassName)).withIdentity(jobClassName, jobGroupName).build();

            // 构建任务执行的CRON表达式
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            // 根据CRON表达式构建触发器
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withDescription(description).withSchedule(scheduleBuilder).build();

            // 在调度器中安排任务
            sched.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            LOG.error("创建定时任务失败:" + e);
            commonResp.setSuccess(false);
            commonResp.setMessage("创建定时任务失败:调度异常");
        } catch (ClassNotFoundException e) {
            LOG.error("创建定时任务失败:" + e);
            commonResp.setSuccess(false);
            commonResp.setMessage("创建定时任务失败：任务类不存在");
        }
        LOG.info("创建定时任务结束：{}", commonResp);
        return commonResp;
    }

    /**
     * 暂停指定的定时任务。
     *
     * @param cronJobReq 包含任务名称和组名的请求体，用于指定要暂停的任务。
     * @return 返回一个通用响应对象，表示操作是否成功及操作结果信息。
     */
    @RequestMapping(value = "/pause")
    public CommonResp pause(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("暂停定时任务开始：{}，{}", jobClassName, jobGroupName);
        CommonResp commonResp = new CommonResp();
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("暂停定时任务失败:" + e);
            commonResp.setSuccess(false);
            commonResp.setMessage("暂停定时任务失败:调度异常");
        }
        LOG.info("暂停定时任务结束：{}", commonResp);
        return commonResp;
    }

    /**
     * 重启指定的定时任务。
     *
     * @param cronJobReq 包含任务名称和组名的请求体，用于指定要重启的任务。
     * @return 返回一个通用响应对象，表示操作是否成功及操作结果信息。
     */
    @RequestMapping(value = "/resume")
    public CommonResp resume(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("重启定时任务开始：{}，{}", jobClassName, jobGroupName);
        CommonResp commonResp = new CommonResp();
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("重启定时任务失败:" + e);
            commonResp.setSuccess(false);
            commonResp.setMessage("重启定时任务失败:调度异常");
        }
        LOG.info("重启定时任务结束：{}", commonResp);
        return commonResp;
    }

    /**
     * 重新安排定时任务的执行。
     *
     * @param cronJobReq 包含定时任务名称、组名、CRON表达式和描述的信息请求对象。
     * @return 返回一个通用响应对象，包含任务是否更新成功的标志和消息。
     */
    @RequestMapping(value = "/reschedule")
    public CommonResp reschedule(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        // 记录更新定时任务开始的日志
        LOG.info("更新定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);
        CommonResp commonResp = new CommonResp();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);

            // 使用CRON表达式构建触发器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTriggerImpl trigger1 = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            trigger1.setStartTime(new Date()); // 重置触发器的开始时间
            CronTrigger trigger = trigger1;

            // 依据新的CRON表达式和描述重新构建触发器
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withDescription(description).withSchedule(scheduleBuilder).build();

            // 用新的触发器重新安排任务执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            // 记录更新定时任务失败的日志
            LOG.error("更新定时任务失败:" + e);
            commonResp.setSuccess(false);
            commonResp.setMessage("更新定时任务失败:调度异常");
        }
        // 记录更新定时任务结束的日志
        LOG.info("更新定时任务结束：{}", commonResp);
        return commonResp;
    }

    /**
     * 删除定时任务。
     *
     * @param cronJobReq 包含要删除的定时任务的名称和组名的请求对象。
     * @return 返回一个通用响应对象，包含操作是否成功和相关消息。
     */
    @RequestMapping(value = "/delete")
    public CommonResp delete(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName(); // 获取任务类名
        String jobGroupName = cronJobReq.getGroup(); // 获取任务组名
        LOG.info("删除定时任务开始：{}，{}", jobClassName, jobGroupName); // 记录开始删除任务的日志
        CommonResp commonResp = new CommonResp(); // 创建通用响应对象
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler(); // 获取调度器
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName)); // 暂停触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName)); // 取消调度
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName)); // 删除任务
        } catch (SchedulerException e) {
            LOG.error("删除定时任务失败:" + e); // 记录删除任务失败的日志
            commonResp.setSuccess(false); // 标记操作失败
            commonResp.setMessage("删除定时任务失败:调度异常"); // 设置失败消息
        }
        LOG.info("删除定时任务结束：{}", commonResp); // 记录删除任务结束的日志
        return commonResp; // 返回操作结果
    }

    /**
     * 查询所有定时任务的信息。
     *
     * 该方法不接受任何参数，通过调用Quartz的Scheduler接口，从调度器中获取所有定时任务的详细信息，
     * 包括任务名称、任务分组、下次触发时间、上次触发时间、Cron表达式、触发器描述及触发器状态等，并将这些信息封装到响应对象中返回。
     *
     * @return CommonResp 包含所有定时任务信息的响应对象。
     */
    @RequestMapping(value="/query")
    public CommonResp query() {
        LOG.info("查看所有定时任务开始");
        CommonResp commonResp = new CommonResp();
        List<CronJobResp> cronJobDtoList = new ArrayList();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            // 遍历所有任务分组
            for (String groupName : scheduler.getJobGroupNames()) {
                // 在当前分组中获取所有任务
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    CronJobResp cronJobResp = new CronJobResp();
                    cronJobResp.setName(jobKey.getName());
                    cronJobResp.setGroup(jobKey.getGroup());

                    // 获取任务的触发器，并从中提取详细信息
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
                    cronJobResp.setNextFireTime(cronTrigger.getNextFireTime());
                    cronJobResp.setPreFireTime(cronTrigger.getPreviousFireTime());
                    cronJobResp.setCronExpression(cronTrigger.getCronExpression());
                    cronJobResp.setDescription(cronTrigger.getDescription());
                    // 获取触发器的当前状态
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                    cronJobResp.setState(triggerState.name());

                    cronJobDtoList.add(cronJobResp);
                }

            }
        } catch (SchedulerException e) {
            LOG.error("查看定时任务失败:" + e);
            commonResp.setSuccess(false);
            commonResp.setMessage("查看定时任务失败:调度异常");
        }
        commonResp.setContent(cronJobDtoList);
        LOG.info("查看定时任务结束：{}", commonResp);
        return commonResp;
    }

}