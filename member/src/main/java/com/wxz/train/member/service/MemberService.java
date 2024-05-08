package com.wxz.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wxz.train.common.enums.BusinessExceptionEnum;
import com.wxz.train.common.exception.BusinessException;
import com.wxz.train.common.utils.SnowUtils;
import com.wxz.train.member.domain.Member;
import com.wxz.train.member.domain.MemberExample;
import com.wxz.train.member.mapper.MemberMapper;
import com.wxz.train.member.req.MemberLoginReq;
import com.wxz.train.member.req.MemberRegisterReq;
import com.wxz.train.member.req.MemberSendCodeReq;
import com.wxz.train.member.resp.MemberLoginResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

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

    /**
     * 发送验证码的方法
     *
     * @param req 包含手机号等信息的请求对象
     *
     * 主要步骤包括：
     * 1. 根据手机号查询会员表，检查手机号是否存在；
     * 2. 如果手机号不存在，则插入一条会员记录；
     * 3. 生成短信验证码；
     * 4. 保存短信记录；
     * 5. 发送短信。
     */
    public void sendCode(MemberSendCodeReq req) {
        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        // 检查手机号是否存在，不存在则插入新记录
        if (CollUtil.isEmpty(list)) {
            log.info("手机号不存在，插入一条记录");
            Member member = new Member();
            member.setId(SnowUtils.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        } else {
            log.info("手机号存在，不插入记录");
        }

        // 生成短信验证码
        // String code = RandomUtil.randomString(4);
        String code = "8888";
        log.info("生成短信验证码：{}", code);

        // 保存短信记录表的逻辑省略实现细节
        log.info("保存短信记录");

        // 对接短信通道，实际发送短信的逻辑省略实现细节
        log.info("对接短信通道，发送短信");
    }

    /**
     * 会员登录功能实现。
     * 根据提供的手机号和验证码对会员进行登录验证。
     *
     * @param req 包含会员手机号和验证码的请求对象。
     * @return 返回登录成功的会员信息响应对象。
     * @throws BusinessException 如果会员不存在或验证码错误，则抛出业务异常。
     */
    public MemberLoginResp login(MemberLoginReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();
        Member memberDB = selectByMobile(mobile);

        // 校验会员是否存在
        if (ObjectUtil.isNull(memberDB)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }

        // 校验验证码是否正确
        if (!"8888".equals(code)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }

        // 返回登录成功的会员信息
        return BeanUtil.copyProperties(memberDB, MemberLoginResp.class);
    }

    /**
     * 根据手机号查询会员信息。
     *
     * @param mobile 要查询的会员手机号。
     * @return 如果找到匹配的会员信息，则返回会员对象；否则返回null。
     */
    private Member selectByMobile(String mobile) {
        // 构建查询条件
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        // 执行查询
        List<Member> list = memberMapper.selectByExample(memberExample);
        // 处理查询结果
        if (CollUtil.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

}