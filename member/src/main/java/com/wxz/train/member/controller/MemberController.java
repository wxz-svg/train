package com.wxz.train.member.controller;

import com.wxz.train.common.resp.CommonResp;
import com.wxz.train.member.req.MemberLoginReq;
import com.wxz.train.member.req.MemberRegisterReq;
import com.wxz.train.member.req.MemberSendCodeReq;
import com.wxz.train.member.resp.MemberLoginResp;
import com.wxz.train.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResp<Integer> count() {
        int count = memberService.count();
        CommonResp<Integer> commonResp = new CommonResp<>();
        commonResp.setContent(count);
        return commonResp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid @RequestBody MemberRegisterReq req) {
        Long register = memberService.register(req);
        CommonResp<Long> commonResp = new CommonResp<>();
        commonResp.setContent(register);
        return commonResp;
    }

    @PostMapping("/send-code")
    public CommonResp<Long> sendCode(@Valid @RequestBody MemberSendCodeReq req) {
        memberService.sendCode(req);
        return new CommonResp<>();
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp resp = memberService.login(req);
        return new CommonResp<>(resp);
    }
}
