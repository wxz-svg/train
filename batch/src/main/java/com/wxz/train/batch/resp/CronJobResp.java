package com.wxz.train.batch.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * CronJobResp 类表示一个计划任务的响应信息。
 * 它包含了任务的各个属性，如组名、任务名、描述、状态、CRON表达式以及下一次和上一次触发时间。
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CronJobResp {
    private String group; // 任务组名
    private String name; // 任务名
    private String description; // 任务描述
    private String state; // 任务状态
    private String cronExpression; // CRON表达式，用于定义任务的执行时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date nextFireTime; // 下一次触发时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date preFireTime; // 上一次触发时间

    /**
     * 生成并返回代表此CronJobResp对象的字符串。
     * @return 表示此对象的字符串。
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CronJobDto{");
        sb.append("cronExpression='").append(cronExpression).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", nextFireTime=").append(nextFireTime);
        sb.append(", preFireTime=").append(preFireTime);
        sb.append('}');
        return sb.toString();
    }

    // Getter和Setter方法为字段提供读取和更新功能。

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getPreFireTime() {
        return preFireTime;
    }

    public void setPreFireTime(Date preFireTime) {
        this.preFireTime = preFireTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
