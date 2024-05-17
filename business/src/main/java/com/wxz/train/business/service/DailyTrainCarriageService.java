package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.DailyTrainCarriage;
import com.wxz.train.business.domain.DailyTrainCarriageExample;
import com.wxz.train.business.domain.TrainCarriage;
import com.wxz.train.business.enums.SeatColEnum;
import com.wxz.train.business.mapper.DailyTrainCarriageMapper;
import com.wxz.train.business.req.DailyTrainCarriageQueryReq;
import com.wxz.train.business.req.DailyTrainCarriageSaveReq;
import com.wxz.train.business.resp.DailyTrainCarriageQueryResp;
import com.wxz.train.common.resp.PageResp;
import com.wxz.train.common.utils.SnowUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainCarriageService.class);

    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

    @Resource
    private TrainCarriageService trainCarriageService;

    /**
     * 保存每日列车车厢信息
     *
     * @param req 包含车厢信息的请求对象，其中应包括座位类型、列数、行数等必要信息。
     *            该方法将根据请求对象中的信息，自动计算出总列数和总座位数，并保存到数据库中。
     *            如果该车厢信息不存在，则插入一条新记录；如果已存在，则更新现有记录。
     */
    public void save(DailyTrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        // 计算总列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getColCount() * req.getRowCount());

        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        if (ObjectUtil.isNull(dailyTrainCarriage.getId())) {
            // 为新记录生成ID并设置创建时间和更新时间
            dailyTrainCarriage.setId(SnowUtils.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        } else {
            // 更新现有记录的更新时间
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.updateByPrimaryKey(dailyTrainCarriage);
        }
    }

    /**
     * 查询每日列车车厢信息列表
     *
     * @param req 包含查询条件、分页信息的请求对象
     * @return 返回分页后的列车车厢信息响应对象
     */
    public PageResp<DailyTrainCarriageQueryResp> queryList(DailyTrainCarriageQueryReq req) {
        // 初始化查询条件
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("date desc, train_code asc, `index` asc");

        // 根据请求参数设置查询条件
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();
        if (ObjUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        // 记录查询的页码和每页条数
        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        // 使用PageHelper进行分页，执行查询
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);

        // 处理查询结果，获取分页信息
        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(dailyTrainCarriageList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 将查询结果转换为响应对象列表
        List<DailyTrainCarriageQueryResp> list = BeanUtil.copyToList(dailyTrainCarriageList, DailyTrainCarriageQueryResp.class);

        // 构建并返回分页响应对象
        PageResp<DailyTrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**
     * 生成指定日期和车次的车厢信息。
     * 此方法首先会删除指定日期和车次的所有既有车厢信息，然后根据提供的车次代码，查询所有的车厢基础数据，
     * 并为每个车厢在指定日期下生成新的车厢信息记录。
     *
     * @param date 指定的日期，用于生成车厢信息。
     * @param trainCode 车次代码，用于查询车厢基础数据并生成相应的车厢信息。
     */
    public void genDaily(Date date, String trainCode) {
        LOG.info("生成日期【{}】车次【{}】的车厢信息开始", DateUtil.formatDate(date), trainCode);

        // 删除指定日期和车次的既有车厢信息
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainCarriageMapper.deleteByExample(dailyTrainCarriageExample);

        // 查询车次的所有车厢基础数据
        List<TrainCarriage> carriageList = trainCarriageService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(carriageList)) {
            LOG.info("该车次没有车厢基础数据，生成该车次的车厢信息结束");
            return;
        }

        // 为每个车厢生成新的车厢信息记录
        for (TrainCarriage trainCarriage : carriageList) {
            DateTime now = DateTime.now();
            DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(trainCarriage, DailyTrainCarriage.class);
            dailyTrainCarriage.setId(SnowUtils.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriage.setDate(date);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        }
        LOG.info("生成日期【{}】车次【{}】的车厢信息结束", DateUtil.formatDate(date), trainCode);
    }
    public void delete(Long id) {
        dailyTrainCarriageMapper.deleteByPrimaryKey(id);
    }
}