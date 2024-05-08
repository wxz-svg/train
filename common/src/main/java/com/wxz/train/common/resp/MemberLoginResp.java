package com.wxz.train.common.resp;

import lombok.Data;

@Data
public class MemberLoginResp {
    private Long id; // 成员的唯一标识ID
    private String mobile; // 成员的手机号码
    private String token; // 成员登录后的token
}
