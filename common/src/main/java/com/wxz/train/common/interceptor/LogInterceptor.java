package com.wxz.train.common.interceptor;

import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 日志拦截器，用于在请求处理之前增加日志流水号，以便追踪日志。
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前执行的逻辑。
     * 主要功能是为当前线程增加一个唯一的日志流水号。
     *
     * @param request  当前HTTP请求对象
     * @param response 当前HTTP响应对象
     * @param handler  将要处理当前请求的处理器对象
     * @return 总是返回true，表示后续处理器可以继续执行
     * @throws Exception 抛出异常时，会中断处理器链的执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 为当前线程增加一个日志流水号，便于追踪整个请求生命周期的日志
        MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));
        return true;
    }

}
