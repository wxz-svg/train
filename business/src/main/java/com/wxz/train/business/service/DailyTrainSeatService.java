package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.DailyTrainSeat;
import com.wxz.train.business.domain.DailyTrainSeatExample;
import com.wxz.train.business.domain.TrainSeat;
import com.wxz.train.business.domain.TrainStation;
import com.wxz.train.business.mapper.DailyTrainSeatMapper;
import com.wxz.train.business.req.DailyTrainSeatQueryReq;
import com.wxz.train.business.req.DailyTrainSeatSaveReq;
import com.wxz.train.business.resp.DailyTrainSeatQueryResp;
import com.wxz.train.common.resp.PageResp;
import com.wxz.train.common.utils.SnowUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainSeatService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainSeatService.class);

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    private TrainSeatService trainSeatService;

    @Resource
    private TrainStationService trainStationService;

    /**
     * 保存每日火车座位信息
     *
     * @param req 包含每日火车座位信息的请求对象
     * 该方法根据传入的每日火车座位保存请求对象，判断是进行座位信息的插入还是更新。
     * 如果请求对象中的ID为null，则认为是新记录，进行插入操作；
     * 如果ID非null，则进行更新操作。
     * 无论是插入还是更新，都会设置创建时间和更新时间为当前时间。
     */
    public void save(DailyTrainSeatSaveReq req) {
        DateTime now = DateTime.now(); // 获取当前时间
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class); // 将请求对象的属性复制到DailyTrainSeat对象中

        if (ObjectUtil.isNull(dailyTrainSeat.getId())) {
            // 如果ID为null，设置ID为新的雪花ID，设置创建时间和更新时间为当前时间，并插入到数据库中
            dailyTrainSeat.setId(SnowUtils.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        } else {
            // 如果ID不为null，设置更新时间为当前时间，并根据主键更新数据库中的记录
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.updateByPrimaryKey(dailyTrainSeat);
        }
    }

    /**
     * 查询每日火车座位信息列表
     *
     * @param req 包含查询条件、分页信息的请求对象
     * @return 返回分页后的座位查询响应对象，包含总行数和座位信息列表
     */
    public PageResp<DailyTrainSeatQueryResp> queryList(DailyTrainSeatQueryReq req) {
        // 初始化查询条件，按火车代码、车厢索引、座位索引升序排序
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("train_code asc, carriage_index asc, carriage_seat_index asc");

        // 创建查询标准
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();

        // 如果提供了火车代码，则添加到查询条件中
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        // 记录查询的页码和每页条数
        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        // 使用PageHelper进行分页，开始查询
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainSeat> dailyTrainSeatList = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);

        // 计算分页信息
        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeatList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 将查询结果转换为响应对象列表
        List<DailyTrainSeatQueryResp> list = BeanUtil.copyToList(dailyTrainSeatList, DailyTrainSeatQueryResp.class);

        // 构建并返回分页响应对象
        PageResp<DailyTrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**
     * 生成指定日期和车次的座位信息。
     * 对于给定的日期和车次，首先会删除已存在的座位信息，然后根据车次查询所有站点信息和座位信息，
     * 为每个座位在指定日期生成一条新的座位记录。
     *
     * @param date 指定的日期
     * @param trainCode 指定的车次代码
     */
    public void genDaily(Date date, String trainCode) {
        LOG.info("生成日期【{}】车次【{}】的座位信息开始", DateUtil.formatDate(date), trainCode);

        // 删除指定日期和车次的原有座位信息
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainSeatMapper.deleteByExample(dailyTrainSeatExample);

        // 获取车次对应的全部站点信息
        List<TrainStation> stationList = trainStationService.selectByTrainCode(trainCode);
        // 根据站点数量，生成用于标记座位销售状态的字符串
        String sell = StrUtil.fillBefore("", '0', stationList.size() - 1);

        // 查询车次的所有座位信息
        List<TrainSeat> seatList = trainSeatService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(seatList)) {
            // 如果查询不到座位信息，则直接结束生成过程
            LOG.info("该车次没有座位，生成该车次的座位信息结束");
            return ;
        }

        // 为每个座位生成新的座位记录，用于指定日期的销售
        for (TrainSeat trainSeat : seatList) {
            DateTime now = DateTime.now();
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
            dailyTrainSeat.setId(SnowUtils.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setSell(sell);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        }
        LOG.info("生成日期【{}】车次【{}】的座位信息结束", DateUtil.formatDate(date), trainCode);
    }



    public void delete(Long id) {
        dailyTrainSeatMapper.deleteByPrimaryKey(id);
    }
}