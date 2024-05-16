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

import java.util.List;

;

@RestController
@RequestMapping("/admin/station")
public class StationAdminController {
    // 注入StationService，用于站点管理的业务逻辑处理
    @Resource
    private StationService stationService;

    /**
     * 保存站点信息
     * @param req 请求体，包含要保存的站点信息
     * @return 返回一个空的响应体，表示操作成功
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody StationSaveReq req) {
        stationService.save(req); // 调用服务层执行保存操作
        return new CommonResp<>();
    }

    /**
     * 查询站点列表
     * @param req 查询请求体，包含查询条件
     * @return 返回站点查询结果的响应体
     */
    @GetMapping("/query-list")
    public CommonResp<PageResp<StationQueryResp>> queryList(@Valid StationQueryReq req) {
        PageResp<StationQueryResp> list = stationService.queryList(req); // 调用服务层查询满足条件的站点列表
        return new CommonResp<>(list);
    }

    /**
     * 查询所有站点信息
     * @return 返回所有站点信息的列表
     */
    @GetMapping("/query-all")
    public CommonResp<List<StationQueryResp>> queryList() {
        List<StationQueryResp> list = stationService.queryAll(); // 调用服务层查询所有站点信息
        return new CommonResp<>(list);
    }

    /**
     * 根据ID删除站点信息
     * @param id 要删除的站点ID
     * @return 返回一个空的响应体，表示操作成功
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        stationService.delete(id); // 调用服务层删除指定ID的站点信息
        return new CommonResp<>();
    }
}
