/**
 * 业务异常枚举类，用于定义业务操作中可能遇到的异常情况
 */
package com.wxz.train.common.enums;


public enum BusinessExceptionEnum {

    /**
     * 成员手机号已存在
     */
    MEMBER_MOBILE_EXIST("手机号已注册");

    private String desc; // 异常描述

    /**
     * 构造方法，初始化异常信息
     * @param desc 异常描述
     */
    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }

    /**
     * 获取异常描述
     * @return 异常描述字符串
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置异常描述（通常不使用，仅提供完整性）
     * @param desc 要设置的异常描述
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 重写toString方法，便于直接打印枚举实例时能提供更友好的信息
     * @return 描述该枚举实例的字符串
     */
    @Override
    public String toString() {
        return "BusinessExceptionEnum{" +
                "desc='" + desc + '\'' +
                "} " + super.toString();
    }
}