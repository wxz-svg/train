package com.wxz.train.common.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


/**
 * 日志切面类，用于拦截Controller层的方法调用，进而添加日志记录。
 */
@Component
@Aspect
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 构造函数，输出日志信息。
     */
    public LogAspect() {
        System.out.println("Common LogAspect");
    }

    /**
     * 定义切点，拦截com.wxz包下所有Controller类的公共方法执行。
     */
    @Pointcut("execution(public * com.wxz..*Controller.*(..))")
    public void controllerPointCut(){
    }

    /**
     * 在目标方法执行前执行的操作。
     * 主要用于记录请求日志。
     *
     * @param joinPoint 切面连接点，表示被拦截的方法。
     */
    @Before("controllerPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        // 添加日志流水号
        // MDC.put("LOG_ID", System.currentTimeMillis() + RandomUtil.randomString(3));

        // 打印请求相关信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        // 打印请求地址、类名方法、远程地址
        log.info("-----------------------开始-------------------------");
        log.info("请求地址:{} {}", request.getRequestURL().toString(), request.getMethod());
        log.info("类名方法:{}.{}", signature.getDeclaringTypeName(), name);
        log.info("远程地址:{}", request.getRemoteAddr());

        // 打印请求参数，排除特殊类型的参数
        Object[] params = joinPoint.getArgs();
        log.info("请求参数:{}", JSON.toJSONString(params));

        Object[] arguments = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof ServletRequest
                    || params[i] instanceof ServletResponse
                    || params[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = params[i];
        }

        // 对参数进行过滤，排除敏感或太长的字段
        String[] excludeProperties = {};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        log.info("请求参数:{}", JSONObject.toJSONString(arguments, excludefilter));
    }

    /**
     * 环绕通知，目标方法执行前后执行的操作。
     * 主要用于记录方法执行时间及返回结果日志。
     *
     * @param proceedingJoinPoint 切面连接点，表示被拦截的方法。
     * @return 返回目标方法的执行结果。
     * @throws Throwable 如果目标方法执行过程中出现异常，则抛出。
     */
    @Around("controllerPointCut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();

        // 对返回结果进行过滤，排除敏感或太长的字段
        String[] excludeProperties = {};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        log.info("返回结果: {}", JSONObject.toJSONString(result, excludefilter));
        log.info("----------------结束 耗时: {} ms--------------", System.currentTimeMillis() - startTime);
        return result;
    }
}