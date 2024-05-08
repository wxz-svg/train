package com.wxz.train.common.context;

import com.wxz.train.common.resp.MemberLoginResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录会员上下文，用于存储和管理当前线程中的登录会员信息。
 */
public class LoginMemberContext {
    // 日志记录器
    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberContext.class);

    // 使用ThreadLocal为每个线程存储一个登录会员信息
    private static ThreadLocal<MemberLoginResp> member = new ThreadLocal<>();

    /**
     * 获取当前线程的登录会员信息。
     *
     * @return 登录会员信息对象，如果未登录则返回null。
     */
    public static MemberLoginResp getMember() {
        return member.get();
    }

    /**
     * 设置当前线程的登录会员信息。
     *
     * @param member 登录会员信息对象。
     */
    public static void setMember(MemberLoginResp member) {
        LoginMemberContext.member.set(member);
    }

    /**
     * 获取当前线程登录会员的ID。
     *
     * @return 登录会员的ID，如果未登录则抛出异常。
     * @throws Exception 如果未登录或获取ID时发生异常，则抛出。
     */
    public static Long getId() {
        try {
            return member.get().getId();
        } catch (Exception e) {
            LOG.error("获取登录会员信息异常", e);
            throw e;
        }
    }

}
