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

import java.util.List;


@RestController
@RequestMapping("/admin/Train")
public class TrainAdminController {

    @Resource
    private TrainService trainService; // 引用TrainService服务

    /**
     * 保存火车信息
     * @param req 保存请求参数，由前端传入，包含火车车次的详细信息
     * @return 返回一个通用响应对象，标识操作是否成功
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainSaveReq req) {
        trainService.save(req); // 调用服务层实现保存逻辑
        return new CommonResp<>(); // 返回成功响应
    }

    /**
     * 查询所有火车车次信息
     * @return
     */
    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResp>> queryList() {
        // 从trainService中查询所有火车车次信息
        List<TrainQueryResp> list = trainService.queryAll();
        // 构造并返回包含查询结果的响应对象
        return new CommonResp<>(list);
    }

    /**
     * 查询火车信息列表
     * @param req 查询请求参数，包含分页和过滤条件等
     * @return 返回一个包含火车信息列表的通用响应对象
     */
    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainQueryResp>> queryList(@Valid TrainQueryReq req) {
        PageResp<TrainQueryResp> list = trainService.queryList(req); // 调用服务层查询火车列表
        return new CommonResp<>(list); // 封装查询结果并返回
    }

    /**
     * 根据ID删除训练信息
     * @param id 要删除的火车信息的ID
     * @return 返回一个通用响应对象，标识操作是否成功
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
       trainService.delete(id); // 调用服务层实现删除逻辑
       return new CommonResp<>(); // 返回成功响应
    }
}
