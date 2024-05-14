package com.wxz.train.business.controller.admin;

import com.wxz.train.business.req.TrainStationQueryReq;
import com.wxz.train.business.req.TrainStationSaveReq;
import com.wxz.train.business.resp.TrainStationQueryResp;
import com.wxz.train.business.service.TrainStationService;
import com.wxz.train.common.resp.CommonResp;
import com.wxz.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

;

@RestController
@RequestMapping("/admin/TrainStation")
public class TrainStationAdminController {
    // 注入TrainStationService，用于处理训练站相关的业务逻辑
    @Resource
    private TrainStationService trainStationService;

    /**
     * 保存火车站信息
     * @param req 包含火车站保存请求数据的对象，由客户端通过RequestBody传入
     * @return 返回一个空的通用响应对象，表示操作完成
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainStationSaveReq req) {
        trainStationService.save(req); // 调用服务层执行保存操作
        return new CommonResp<>(); // 返回成功响应
    }

    /**
     * 查询火车站信息列表
     * @param req 包含火车站查询请求条件的对象
     * @return 返回火车站信息列表的通用响应对象
     */
    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainStationQueryResp>> queryList(@Valid TrainStationQueryReq req) {
        PageResp<TrainStationQueryResp> list = trainStationService.queryList(req); // 调用服务层执行查询操作
        return new CommonResp<>(list); // 返回查询结果的响应
    }

    /**
     * 根据ID删除火车站信息
     * @param id 要删除的火车站的ID，从URL路径变量获取
     * @return 返回一个空的通用响应对象，表示操作完成
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
       trainStationService.delete(id); // 调用服务层执行删除操作
       return new CommonResp<>(); // 返回成功响应
    }
}
