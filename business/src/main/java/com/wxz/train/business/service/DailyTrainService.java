package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.DailyTrain;
import com.wxz.train.business.domain.DailyTrainExample;
import com.wxz.train.business.domain.Train;
import com.wxz.train.business.mapper.DailyTrainMapper;
import com.wxz.train.business.req.DailyTrainQueryReq;
import com.wxz.train.business.req.DailyTrainSaveReq;
import com.wxz.train.business.resp.DailyTrainQueryResp;
import com.wxz.train.common.resp.PageResp;
import com.wxz.train.common.utils.SnowUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainService.class);

    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Resource
    private TrainService trainService;

    public void save(DailyTrainSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        if (ObjectUtil.isNull(dailyTrain.getId())) {
            dailyTrain.setId(SnowUtils.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
        } else {
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKey(dailyTrain);
        }
    }

    public PageResp<DailyTrainQueryResp> queryList(DailyTrainQueryReq req) {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("date desc, code asc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getCode())) {
            criteria.andCodeEqualTo(req.getCode());
        }

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(dailyTrainExample);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrainList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainQueryResp> list = BeanUtil.copyToList(dailyTrainList, DailyTrainQueryResp.class);

        PageResp<DailyTrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainMapper.deleteByPrimaryKey(id);
    }

    /**
     * 生成某日所有车次的信息，包括车次、车站、车厢和座位信息。
     * @param date 指定日期，用于生成该日期的车次信息。
     */
    public void genDaily(Date date) {
        // 从服务层获取所有车次的基础数据
        List<Train> trainList = trainService.selectAll();
        // 若没有车次基础数据，则记录日志并结束任务
        if (CollUtil.isEmpty(trainList)) {
            LOG.info("没有车次基础数据，任务结束");
            return ;
        }

        // 遍历所有车次，为指定日期生成车次信息
        for (Train train : trainList) {
            genDailyTrain(date, train);
        }
    }

    /**
     * 为指定车次和日期生成详细的每日车次信息。
     * @param date 指定日期，用于生成该车次在该日期的信息。
     * @param train 指定的车次对象，包含车次的基础信息。
     */
    public void genDailyTrain(Date date, Train train) {
        // 首先删除该车次在指定日期已有的信息
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.createCriteria()
                .andDateEqualTo(date)
                .andCodeEqualTo(train.getCode());
        dailyTrainMapper.deleteByExample(dailyTrainExample);

        // 生成并插入新的车次信息
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtils.getSnowflakeNextId()); // 为新记录生成唯一ID
        dailyTrain.setCreateTime(now); // 设置创建时间
        dailyTrain.setUpdateTime(now); // 设置更新时间
        dailyTrain.setDate(date); // 设置日期
        dailyTrainMapper.insert(dailyTrain); // 插入新的车次信息
    }
}