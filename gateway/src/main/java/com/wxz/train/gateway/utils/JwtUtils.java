package com.wxz.train.gateway.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
public class JwtUtils {
    // 日志记录器
    private static final Logger LOG = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * 盐值很重要，不能泄漏，且每个项目都应该不一样，可以放到配置文件中
     */
    private static final String key = "moonke12306";

    /**
     * 创建一个JWT Token。
     * @param id 用户的ID。
     * @param mobile 用户的手机号。
     * @return 生成的JWT Token字符串。
     */
    public static String createToken(Long id, String mobile) {
        DateTime now = DateTime.now();
        DateTime expTime = now.offsetNew(DateField.SECOND, 10);
        Map<String, Object> payload = new HashMap<>();
        // 初始化Token负载信息，包括签发时间、过期时间、生效时间和用户ID、手机号
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, expTime);
        payload.put(JWTPayload.NOT_BEFORE, now);
        payload.put("id", id);
        payload.put("mobile", mobile);
        String token = JWTUtil.createToken(payload, key.getBytes());
        LOG.info("生成JWT token：{}", token);
        return token;
    }

    /**
     * 验证JWT Token的有效性。
     * @param token 待验证的JWT Token。
     * @return 如果Token有效返回true，否则返回false。
     */
    public static boolean validate(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        // 使用盐值验证JWT Token
        boolean validate = jwt.validate(0);
        LOG.info("JWT token校验结果：{}", validate);
        return validate;
    }

    /**
     * 从JWT Token中获取原始负载信息。
     * @param token 待处理的JWT Token。
     * @return 包含JWT Token负载信息的JSONObject，不包括签发时间、过期时间和生效时间。
     */
    public static JSONObject getJSONObject(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        JSONObject payloads = jwt.getPayloads();
        // 从负载中移除签发时间、过期时间和生效时间字段
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);
        LOG.info("根据token获取原始内容：{}", payloads);
        return payloads;
    }


/*    public static void main(String[] args) {
        createToken(1L, "123");

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE2NzY4OTk4MjcsIm1vYmlsZSI6IjEyMyIsImlkIjoxLCJleHAiOjE2NzY4OTk4MzcsImlhdCI6MTY3Njg5OTgyN30.JbFfdeNHhxKhAeag63kifw9pgYhnNXISJM5bL6hM8eU";
        validate(token);

        getJSONObject(token);
    }*/
}