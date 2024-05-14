package com.wxz.train.business.controller.admin;

import com.wxz.train.business.req.StationQueryReq;
import com.wxz.train.business.req.StationSaveReq;
import com.wxz.train.business.resp.StationQueryResp;
import com.wxz.train.business.service.StationService;
import com.wxz.train.common.resp.CommonResp;
import com.wxz.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/Station")
public class StationAdminController {

    @Resource
    private StationService stationService; // 引用站务服务

    /**
     * 保存车站信息
     * @param req 车站保存请求对象，包含要保存的车站信息
     * @return 返回一个通用响应对象，表示操作是否成功
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody StationSaveReq req) {
        stationService.save(req); // 调用服务保存车站信息
        return new CommonResp<>(); // 返回成功响应
    }

    /**
     * 查询车站信息列表
     * @param req 车站查询请求对象，包含查询条件
     * @return 返回一个包含车站信息列表的通用响应对象
     */
    @GetMapping("/query-list")
    public CommonResp<PageResp<StationQueryResp>> queryList(@Valid StationQueryReq req) {
        PageResp<StationQueryResp> list = stationService.queryList(req); // 调用服务查询车站信息列表
        return new CommonResp<>(list); // 返回查询结果
    }

    /**
     * 根据ID删除车站信息
     * @param id 要删除的车站ID
     * @return 返回一个通用响应对象，表示操作是否成功
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
       stationService.delete(id); // 调用服务删除指定ID的车站信息
       return new CommonResp<>(); // 返回成功响应
    }
}
