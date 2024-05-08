package com.wxz.train.member.resp;

import lombok.Data;

/**
 * 成员登录响应类
 * 用于封装成员登录后的基本信息
 */
@Data
public class MemberLoginResp {
    private Long id; // 成员的唯一标识ID
    private String mobile; // 成员的手机号码
    private String token; // 成员登录后的token
}