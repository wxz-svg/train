package com.wxz.train.common.handler;

import com.wxz.train.common.exception.BusinessException;
import com.wxz.train.common.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 * @ControllerAdvice 注解表示这是一个全局异常处理类，它会捕获所有控制器中抛出的异常。
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 处理所有未捕获的异常
     * @param e 抛出的异常对象
     * @return 返回一个封装了异常信息的CommonResp对象
     * @throws Exception 将异常继续向上抛出，以便于在更高层次上处理（如需要记录日志等）
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp exceptionhandler(Exception e){
        CommonResp commonResp = new CommonResp<>();
        // 记录异常日志
        log.error("系统异常", e);
        commonResp.setSuccess(false);
        commonResp.setMessage("系统异常, 请联系管理员");
        return commonResp;
    }

    /**
     * 处理业务逻辑层抛出的异常
     * @param e 抛出的业务异常对象
     * @return 返回一个封装了业务异常信息的CommonResp对象
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResp exceptionHandler(BusinessException e) {
        CommonResp commonResp = new CommonResp();
        log.error("业务异常：{}", e.getE().getDesc());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getE().getDesc());
        return commonResp;
    }

}