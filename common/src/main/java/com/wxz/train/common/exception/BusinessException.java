package com.wxz.train.common.exception;

import com.wxz.train.common.enums.BusinessExceptionEnum;

/**
 * 业务异常类，用于处理业务层面上的异常。
 * 这个类封装了业务异常的错误信息和代码。
 */
public class BusinessException extends RuntimeException{

    private BusinessExceptionEnum exceptionEnum; // 存储异常的枚举类型

    /**
     * 构造函数，初始化业务异常。
     *
     * @param exceptionEnum 异常枚举类型，定义了异常的类型和错误信息。
     */
    public BusinessException (BusinessExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    /**
     * 获取异常的枚举类型。
     *
     * @return 返回异常的枚举类型，包含了异常的类型和错误信息。
     */
    public BusinessExceptionEnum getE() {
        return exceptionEnum;
    }

    /**
     * 设置异常的枚举类型。
     *
     * @param exceptionEnum 需要设置的异常枚举类型。
     */
    public void setExceptionEnum(BusinessExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    /**
     * 不写入堆栈信息，提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}