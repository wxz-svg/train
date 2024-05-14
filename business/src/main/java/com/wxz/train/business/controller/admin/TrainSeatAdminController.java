package com.wxz.train.business.controller.admin;

import com.wxz.train.business.req.TrainSeatQueryReq;
import com.wxz.train.business.req.TrainSeatSaveReq;
import com.wxz.train.business.resp.TrainSeatQueryResp;
import com.wxz.train.business.service.TrainSeatService;
import com.wxz.train.common.resp.CommonResp;
import com.wxz.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

;

@RestController
@RequestMapping("/admin/TrainSeat")
public class TrainSeatAdminController {
    // 注入TrainSeatService，用于处理火车座位相关的业务逻辑
    @Resource
    private TrainSeatService trainSeatService;

    /**
     * 保存火车座位信息
     * @param req 包含火车座位保存信息的请求体
     * @return 返回一个通用响应体，标识操作是否成功
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainSeatSaveReq req) {
        trainSeatService.save(req); // 调用服务层执行保存操作
        return new CommonResp<>(); // 返回成功响应
    }

    /**
     * 查询火车座位列表
     * @param req 包含火车座位查询条件的请求体
     * @return 返回一个包含火车座位查询结果的分页响应体
     */
    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainSeatQueryResp>> queryList(@Valid TrainSeatQueryReq req) {
        PageResp<TrainSeatQueryResp> list = trainSeatService.queryList(req); // 调用服务层查询座位列表
        return new CommonResp<>(list); // 封装查询结果并返回
    }

    /**
     * 根据ID删除火车座位信息
     * @param id 要删除的火车座位的ID
     * @return 返回一个通用响应体，标识操作是否成功
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
       trainSeatService.delete(id); // 调用服务层执行删除操作
       return new CommonResp<>(); // 返回成功响应
    }
}
