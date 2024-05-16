package com.wxz.train.business.controller.admin;

import com.wxz.train.business.req.TrainQueryReq;
import com.wxz.train.business.req.TrainSaveReq;
import com.wxz.train.business.resp.TrainQueryResp;
import com.wxz.train.business.service.TrainSeatService;
import com.wxz.train.business.service.TrainService;
import com.wxz.train.common.resp.CommonResp;
import com.wxz.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

;

@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {

    @Resource
    private TrainService trainService; // 注入TrainService，用于处理火车相关的服务

    @Resource
    private TrainSeatService trainSeatService; // 注入TrainSeatService，用于处理火车座位相关的服务

    /**
     * 保存火车信息。
     * @param req 验证后的火车保存请求数据，包含要保存的火车信息。
     * @return 返回一个通用响应对象，表示操作结果。
     */
    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainSaveReq req) {
        trainService.save(req);
        return new CommonResp<>();
    }

    /**
     * 查询火车信息列表。
     * @param req 查询请求参数，用于过滤查询结果。
     * @return 返回一个包含火车查询结果的分页响应对象。
     */
    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainQueryResp>> queryList(@Valid TrainQueryReq req) {
        PageResp<TrainQueryResp> list = trainService.queryList(req);
        return new CommonResp<>(list);
    }

    /**
     * 根据ID删除火车信息。
     * @param id 要删除的火车的ID。
     * @return 返回一个通用响应对象，表示操作结果。
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
       trainService.delete(id);
       return new CommonResp<>();
    }

    /**
     * 查询所有火车信息。
     * @return 返回一个包含所有火车查询结果的列表的通用响应对象。
     */
    @GetMapping("/query-all")
    public CommonResp<List<TrainQueryResp>> queryList() {
        List<TrainQueryResp> list = trainService.queryAll();
        return new CommonResp<>(list);
    }

    /**
     * 为指定火车生成座位。
     * @param trainCode 火车代码，用于标识要生成座位的火车。
     * @return 返回一个通用响应对象，表示操作结果。
     */
    @GetMapping("/gen-seat/{trainCode}")
    public CommonResp<Object> genSeat(@PathVariable String trainCode) {
        trainSeatService.genTrainSeat(trainCode);
        return new CommonResp<>();
    }

}
