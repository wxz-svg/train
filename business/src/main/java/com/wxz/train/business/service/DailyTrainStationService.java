package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.DailyTrainStation;
import com.wxz.train.business.domain.DailyTrainStationExample;
import com.wxz.train.business.domain.TrainStation;
import com.wxz.train.business.mapper.DailyTrainStationMapper;
import com.wxz.train.business.req.DailyTrainStationQueryReq;
import com.wxz.train.business.req.DailyTrainStationSaveReq;
import com.wxz.train.business.resp.DailyTrainStationQueryResp;
import com.wxz.train.common.resp.PageResp;
import com.wxz.train.common.utils.SnowUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainStationService.class);

    @Resource
    private DailyTrainStationMapper dailyTrainStationMapper;

    @Resource
    private TrainStationService trainStationService;

    public void save(DailyTrainStationSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(req, DailyTrainStation.class);
        if (ObjectUtil.isNull(dailyTrainStation.getId())) {
            dailyTrainStation.setId(SnowUtils.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.insert(dailyTrainStation);
        } else {
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.updateByPrimaryKey(dailyTrainStation);
        }
    }

    /**
     * 查询每日列车车站信息列表
     *
     * @param req 包含查询条件、分页信息的请求对象
     * @return 返回包含查询结果和总条数的分页响应对象
     */
    public PageResp<DailyTrainStationQueryResp> queryList(DailyTrainStationQueryReq req) {
        // 初始化查询条件
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.setOrderByClause("date desc, train_code asc, `index` asc");

        // 根据请求参数设置查询条件
        DailyTrainStationExample.Criteria criteria = dailyTrainStationExample.createCriteria();
        if (ObjUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        // 记录查询的页码和每页条数
        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        // 使用PageHelper进行分页查询
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainStation> dailyTrainStationList = dailyTrainStationMapper.selectByExample(dailyTrainStationExample);

        // 计算分页信息
        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(dailyTrainStationList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 将查询结果转换为响应对象列表
        List<DailyTrainStationQueryResp> list = BeanUtil.copyToList(dailyTrainStationList, DailyTrainStationQueryResp.class);

        // 构建分页响应对象
        PageResp<DailyTrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**
     * 生成指定日期和车次的车站信息。
     * 对于给定的日期和车次，首先会删除已存在的车站信息，然后根据车次查询所有的车站信息，并将这些信息插入到当天的车站信息表中。
     * 如果该车次没有车站基础数据，则不进行任何操作。
     *
     * @param date 指定的日期
     * @param trainCode 车次代码
     */
    public void genDaily(Date date, String trainCode) {
        LOG.info("生成日期 [{}] 车次 [{}] 的车站信息开始", DateUtil.formatDate(date), trainCode);

        // 删除指定日期和车次的车站信息
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainStationMapper.deleteByExample(dailyTrainStationExample);

        // 获取并处理该车次的所有车站信息
        List<TrainStation> stationList = trainStationService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(stationList)) {
            LOG.info("该车次没有车站基础数据,生成该车次的车站信息结束");
            return ;
        }

        // 插入该车次的车站信息
        for (TrainStation trainStation : stationList) {
            DateTime now = DateTime.now();
            DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrainStation.setId(SnowUtils.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStation.setDate(date);
            dailyTrainStationMapper.insert(dailyTrainStation);
        }
        LOG.info("生成日期 [{}] 车次 [{}] 的车站信息结束", DateUtil.formatDate(date), trainCode);
    }

    public void delete(Long id) {
        dailyTrainStationMapper.deleteByPrimaryKey(id);
    }
}