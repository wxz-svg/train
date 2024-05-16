package com.wxz.train.batch.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Scheduler配置类，用于配置Quartz调度器。
 */
@Configuration
public class SchedulerConfig {

    // 注入自定义作业工厂
    @Resource
    private MyJobFactory myJobFactory;

    /**
     * 配置SchedulerFactoryBean。
     *
     * @param dataSource 用于Quartz调度器的数据库数据源。
     * @return SchedulerFactoryBean实例，配置了数据源、作业工厂和启动延迟。
     * @throws IOException 如果配置过程中发生IO错误。
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource); // 设置数据源
        factory.setJobFactory(myJobFactory); // 设置作业工厂
        factory.setStartupDelay(2); // 设置启动延迟时间为2秒
        return factory;
    }
}
