package com.wxz.train.common.config;

import com.wxz.train.common.interceptor.LogInterceptor;
import com.wxz.train.common.interceptor.MemberInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置类，用于自定义WebMvcConfigurer接口的实现，进而配置拦截器等。
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    // 注入MemberInterceptor实例，用于后续的拦截器配置
    @Resource
    private MemberInterceptor memberInterceptor;

    @Resource
    private LogInterceptor logInterceptor;

    /**
     * 配置拦截器。
     * @param registry 拦截器注册表，用于向Spring MVC注册拦截器及其拦截规则。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);

        // 向拦截器注册表添加拦截器实例，并配置拦截规则
        registry.addInterceptor(memberInterceptor)
                // 指定拦截器拦截所有请求
                .addPathPatterns("/**")
                // 排除某些特定路径的拦截
                .excludePathPatterns(
                        "/member/hello", // 排除示例路径1
                        "/member/member/send-code", // 排除示例路径2
                        "/member/member/login" // 排除示例路径3
                );
    }
}