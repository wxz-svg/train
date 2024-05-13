package com.wxz.train.member.controller;

import com.wxz.train.common.context.LoginMemberContext;
import com.wxz.train.common.resp.CommonResp;
import com.wxz.train.member.req.PassengerQueryReq;
import com.wxz.train.member.req.PassengerSaveReq;
import com.wxz.train.member.resp.PassengerQueryResp;
import com.wxz.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    /**
     * 保存乘客信息
     * @param req
     * @return
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq req) {
        passengerService.save(req);
        return new CommonResp<>();
    }

    /**
     * 查询乘客列表
     * @param req
     * @return
     */
    @GetMapping("/query-list")
    public CommonResp<List<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        List<PassengerQueryResp> list = passengerService.queryList(req);
        return new CommonResp<>(list);
    }


}