package com.wxz.train.business.controller.admin;

import com.wxz.train.business.req.TrainQueryReq;
import com.wxz.train.business.req.TrainSaveReq;
import com.wxz.train.business.resp.TrainQueryResp;
import com.wxz.train.business.service.TrainService;
import com.wxz.train.common.resp.CommonResp;
import com.wxz.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/Train")
public class TrainAdminController {

    @Resource
    private TrainService trainService; // 引用TrainService服务

    /**
     * 保存训练信息
     * @param req 训练保存请求参数，由前端传入，包含训练的详细信息
     * @return 返回一个通用响应对象，标识操作是否成功
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainSaveReq req) {
        trainService.save(req); // 调用服务层实现保存逻辑
        return new CommonResp<>(); // 返回成功响应
    }

    /**
     * 查询训练信息列表
     * @param req 训练查询请求参数，包含分页和过滤条件等
     * @return 返回一个包含训练信息列表的通用响应对象
     */
    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainQueryResp>> queryList(@Valid TrainQueryReq req) {
        PageResp<TrainQueryResp> list = trainService.queryList(req); // 调用服务层查询训练列表
        return new CommonResp<>(list); // 封装查询结果并返回
    }

    /**
     * 根据ID删除训练信息
     * @param id 要删除的训练信息的ID
     * @return 返回一个通用响应对象，标识操作是否成功
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
       trainService.delete(id); // 调用服务层实现删除逻辑
       return new CommonResp<>(); // 返回成功响应
    }
}
