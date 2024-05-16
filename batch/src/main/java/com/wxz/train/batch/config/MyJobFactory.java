package com.wxz.train.batch.config;

import jakarta.annotation.Resource;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * 自定义的Job工厂类，继承自SpringBeanJobFactory，用于在Quartz作业实例创建后进行自动装配。
 */
@Component
public class MyJobFactory extends SpringBeanJobFactory {

    @Resource
    private AutowireCapableBeanFactory beanFactory; // 注入Spring的BeanFactory，用于自动装配Job实例

    /**
     * 覆盖了父类的createJobInstance方法，以便在创建作业实例后对其进行自动装配。
     *
     * @param bundle 触发器触发时的绑定包，包含作业和触发器等信息。
     * @return 装配后的作业实例。
     * @throws Exception 如果创建或装配作业实例时发生错误。
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle); // 从父类创建作业实例
        beanFactory.autowireBean(jobInstance); // 对创建的作业实例进行自动装配
        return jobInstance;
    }
}
