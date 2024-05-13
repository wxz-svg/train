package com.wxz.train.common.context;

import com.wxz.train.common.resp.MemberLoginResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录会员上下文，用于存储和管理当前线程中的登录会员信息。
 */
public class LoginMemberContext {
    private static final Logger log = LoggerFactory.getLogger(LoginMemberContext.class);
    private static final ThreadLocal<MemberLoginResp> member = new ThreadLocal<>();

    public static MemberLoginResp getMember() {
        return member.get();
    }

    public static void setMember(MemberLoginResp member) {
        LoginMemberContext.member.set(member);
    }

    public static Long getId() {
        try {
            return member.get().getId();
        } catch (Exception e) {
            log.error("获取登录会员信息异常", e);
            throw e;
        }
    }


//    /**
//     * 获取当前线程的登录会员信息。
//     *
//     * @return 登录会员信息对象，如果未登录则返回null。
//     */
//    public static MemberLoginResp getMember() {
//        MemberLoginResp loginMember = member.get();
//        if (loginMember == null) {
//            log.info("当前线程无登录会员信息");
//        } else {
//            log.debug("当前线程的登录会员信息: {}", loginMember.toString());
//        }
//        return loginMember;
//    }
//
//    /**
//     * 设置当前线程的登录会员信息。
//     *
//     * @param member 登录会员信息对象。
//     */
//    public static void setMember(MemberLoginResp member) {
//        if (member == null) {
//            member = new MemberLoginResp(); // 创建新的MemberLoginResp对象
//            log.warn("创建了一个新的默认MemberLoginResp对象，因为传入的参数为null");
//        }
//        LoginMemberContext.member.set(member);
//    }
//
//
//    /**
//     * 获取当前线程登录会员的ID。
//     *
//     * @return 登录会员的ID，如果未登录则抛出异常。
//     * @throws NullPointerException 如果当前线程没有关联的登录会员信息。
//     */
//    public static Long getId() {
//        MemberLoginResp loginMember = member.get();
//        if (loginMember == null) {
//            log.warn("尝试获取登录会员ID，但当前线程无登录会员信息");
//            throw new NullPointerException("当前线程无登录会员信息");
//        }
//        try {
//            return loginMember.getId();
//        } catch (NullPointerException e) {
//            log.error("获取登录会员ID时出现异常，可能是MemberLoginResp对象不完整", e);
//            throw e;
//        }
//    }
//
//    public static void remove() {
//        member.remove();
//    }
}