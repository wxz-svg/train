package com.wxz.train.batch.req;

/**
 * CronJobReq 类用于定义一个定时任务的请求参数
 */
public class CronJobReq {
    // 定时任务所属的分组
    private String group;

    // 定时任务的名称
    private String name;

    // 定时任务的描述信息
    private String description;

    // 定时任务的CRON表达式
    private String cronExpression;

    /**
     * 重写toString方法，用于返回该对象的字符串表示
     * @return 描述该对象的字符串，包含cronExpression、group、name和description属性的值
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CronJobDto{");
        sb.append("cronExpression='").append(cronExpression).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    // 获取定时任务所属的分组
    public String getGroup() {
        return group;
    }

    // 设置定时任务所属的分组
    public void setGroup(String group) {
        this.group = group;
    }

    // 获取定时任务的CRON表达式
    public String getCronExpression() {
        return cronExpression;
    }

    // 设置定时任务的CRON表达式
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    // 获取定时任务的描述信息
    public String getDescription() {
        return description;
    }

    // 设置定时任务的描述信息
    public void setDescription(String description) {
        this.description = description;
    }

    // 获取定时任务的名称
    public String getName() {
        return name;
    }

    // 设置定时任务的名称
    public void setName(String name) {
        this.name = name;
    }
}
