package com.wxz.train.common.utils;

import cn.hutool.core.util.IdUtil;

/**
 * 封装hutool雪花算法工具类
 * 雪花算法是一种分布式ID生成算法，生成的ID是全局唯一、递增的长整型数字。
 */
public class SnowUtils {

    private static long dataCenterId = 1;  //数据中心ID，用于区分不同的数据中心
    private static long workerId = 1;     //机器标识ID，用于区分同一数据中心内的不同机器

    /**
     * 获取下一个雪花算法生成的长整型ID
     *
     * @return 雪花算法生成的全局唯一、递增的长整型ID
     */
    public static long getSnowflakeNextId() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }

    /**
     * 获取下一个雪花算法生成的字符串形式的ID
     *
     * @return 雪花算法生成的全局唯一、递增的字符串形式的ID
     */
    public static String getSnowflakeNextIdStr() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextIdStr();
    }
}