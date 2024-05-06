package com.wxz.train.member.service;

import cn.hutool.core.collection.CollUtil;
import com.wxz.train.common.enums.BusinessExceptionEnum;
import com.wxz.train.common.exception.BusinessException;
import com.wxz.train.common.utils.SnowUtils;
import com.wxz.train.member.domain.Member;
import com.wxz.train.member.domain.MemberExample;
import com.wxz.train.member.mapper.MemberMapper;
import com.wxz.train.member.req.MemberRegisterReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    public int count(){
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    /**
     * 会员注册
     * @param req 注册请求实体类
     * @return 返回新注册会员的ID
     */
    public Long register(MemberRegisterReq req) {
        // 根据手机号查询已存在的会员信息
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(req.getMobile());
        List<Member> list = memberMapper.selectByExample(memberExample);
        // 如果查询结果不为空，表示手机号已注册，抛出异常
        if (CollUtil.isNotEmpty(list)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        // 创建新的会员对象，并设置相关信息
        Member member = new Member();
        member.setId(SnowUtils.getSnowflakeNextId());
        member.setMobile(req.getMobile());

        // 将新会员信息插入数据库
        memberMapper.insert(member);
        // 返回新注册会员的ID
        return member.getId();
    }
}