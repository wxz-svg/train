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
@RequestMapping("/admin/train-station")
public class TrainStationAdminController {

    @Resource
    private TrainStationService trainStationService;

    /**
     * 保存火车站点信息。
     * @param req 包含火车站点保存请求数据的验证过的请求体。
     * @return 返回一个空的通用响应体，表示操作成功。
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainStationSaveReq req) {
        trainStationService.save(req); // 调用服务层实现保存逻辑
        return new CommonResp<>();
    }

    /**
     * 查询火车站点列表。
     * @param req 包含火车站点查询请求数据的请求体。
     * @return 返回火车站点查询结果的分页响应体。
     */
    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainStationQueryResp>> queryList(@Valid TrainStationQueryReq req) {
        PageResp<TrainStationQueryResp> list = trainStationService.queryList(req); // 调用服务层查询列表
        return new CommonResp<>(list);
    }

    /**
     * 根据ID删除火车站点。
     * @param id 要删除的火车站点的ID。
     * @return 返回一个空的通用响应体，表示操作成功。
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
       trainStationService.delete(id); // 调用服务层实现删除逻辑
       return new CommonResp<>();
    }
}
