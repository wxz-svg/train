package com.wxz.train.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wxz.train.common.context.LoginMemberContext;
import com.wxz.train.common.resp.MemberLoginResp;
import com.wxz.train.common.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器：用于处理请求前的逻辑，例如登录状态验证。
 * Spring框架特有的，常用于登录校验，权限校验，请求日志打印等。
 */
@Component
public class MemberInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(MemberInterceptor.class);

    /**
     * 在请求处理之前进行预处理。
     * 主要用于校验请求中的登录状态。
     *
     * @param request  HttpServletRequest，代表客户端的请求
     * @param response HttpServletResponse，用于向客户端发送响应
     * @param handler  将要处理请求的处理器对象
     * @return boolean，如果返回true，则继续处理请求；如果返回false，则中断请求的处理。
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = request.getHeader("token");
        // 当token非空时，进行一系列的登录状态处理
        if (StrUtil.isNotBlank(token)) {
            log.info("获取会员登录token：{}", token);
            // 解析token，获取登录会员信息
            JSONObject loginMember = JwtUtils.getJSONObject(token);
            log.info("当前登录会员：{}", loginMember);
            // 将登录会员信息设置到上下文中，以供后续处理使用
            MemberLoginResp member = JSONUtil.toBean(loginMember, MemberLoginResp.class);
            LoginMemberContext.setMember(member);
        }
        // 允许请求继续处理
        return true;
    }

}