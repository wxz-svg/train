package com.wxz.train.business.controller.admin;

import com.wxz.train.business.req.TrainCarriageQueryReq;
import com.wxz.train.business.req.TrainCarriageSaveReq;
import com.wxz.train.business.resp.TrainCarriageQueryResp;
import com.wxz.train.business.service.TrainCarriageService;
import com.wxz.train.common.resp.CommonResp;
import com.wxz.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageAdminController {

    @Resource
    private TrainCarriageService trainCarriageService; // 依赖注入TrainCarriageService服务

    @PostMapping("/save")
    /**
     * 保存火车车厢信息。
     * @param req 包含火车车厢保存信息的请求体。
     * @return 返回一个通用响应体，表示操作结果。
     */
    public CommonResp<Object> save(@Valid @RequestBody TrainCarriageSaveReq req) {
        trainCarriageService.save(req); // 调用服务层保存火车车厢信息
        return new CommonResp<>(); // 返回成功响应
    }

    @GetMapping("/query-list")
    /**
     * 查询火车车厢信息列表。
     * @param req 包含火车车厢查询条件的请求体。
     * @return 返回一个包含火车车厢查询结果的分页响应体。
     */
    public CommonResp<PageResp<TrainCarriageQueryResp>> queryList(@Valid TrainCarriageQueryReq req) {
        PageResp<TrainCarriageQueryResp> list = trainCarriageService.queryList(req); // 调用服务层查询火车车厢信息
        return new CommonResp<>(list); // 返回查询结果的响应
    }

    @DeleteMapping("/delete/{id}")
    /**
     * 根据ID删除火车车厢信息。
     * @param id 要删除的火车车厢的ID。
     * @return 返回一个通用响应体，表示操作结果。
     */
    public CommonResp<Object> delete(@PathVariable Long id) {
       trainCarriageService.delete(id); // 调用服务层删除指定ID的火车车厢信息
       return new CommonResp<>(); // 返回成功响应
    }
}
