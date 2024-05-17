package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.TrainCarriage;
import com.wxz.train.business.domain.TrainSeat;
import com.wxz.train.business.domain.TrainSeatExample;
import com.wxz.train.business.enums.SeatColEnum;
import com.wxz.train.business.mapper.TrainSeatMapper;
import com.wxz.train.business.req.TrainSeatQueryReq;
import com.wxz.train.business.req.TrainSeatSaveReq;
import com.wxz.train.business.resp.TrainSeatQueryResp;
import com.wxz.train.common.resp.PageResp;
import com.wxz.train.common.utils.SnowUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainSeatService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainSeatService.class);

    @Resource
    private TrainSeatMapper trainSeatMapper;

    @Resource
    private TrainCarriageService trainCarriageService;

    /**
     * 保存火车座位信息。
     *
     * @param req 包含火车座位信息的请求对象。该对象可能包含座位的ID，以及其他座位相关的信息。
     *            如果ID为null，则视为新增座位；如果ID不为null，则视为更新现有座位信息。
     *
     * 该方法首先判断传入的座位信息是否已有ID，无ID则视为新记录，生成ID并插入数据库；
     * 有ID则视为更新记录，更新修改时间并更新数据库中对应记录。
     */
    public void save(TrainSeatSaveReq req) {
        DateTime now = DateTime.now(); // 获取当前时间
        TrainSeat trainSeat = BeanUtil.copyProperties(req, TrainSeat.class); // 将请求对象的属性复制到TrainSeat对象中

        if (ObjectUtil.isNull(trainSeat.getId())) {
            // 处理新记录
            trainSeat.setId(SnowUtils.getSnowflakeNextId()); // 生成唯一ID
            trainSeat.setCreateTime(now); // 设置创建时间
            trainSeat.setUpdateTime(now); // 设置更新时间
            trainSeatMapper.insert(trainSeat); // 插入新记录
        } else {
            // 处理更新记录
            trainSeat.setUpdateTime(now); // 更新更新时间
            trainSeatMapper.updateByPrimaryKey(trainSeat); // 根据主键更新记录
        }
    }

    /**
     * 查询火车座位列表
     *
     * @param req 包含查询条件、分页信息的请求对象
     * @return 返回火车座位的分页响应对象，包含总行数和座位查询响应列表
     */
    public PageResp<TrainSeatQueryResp> queryList(TrainSeatQueryReq req) {
        // 初始化查询条件，按火车编码、车厢索引、座位索引升序排序
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("train_code asc, carriage_index asc, carriage_seat_index asc");

        // 创建查询标准，并根据请求对象中的火车编码进行过滤
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        // 记录查询的页码和每页条数
        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        // 使用分页插件开始分页，并根据查询条件获取座位列表
        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainSeat> trainSeatList = trainSeatMapper.selectByExample(trainSeatExample);

        // 计算分页信息
        PageInfo<TrainSeat> pageInfo = new PageInfo<>(trainSeatList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 将座位实体列表转换为查询响应列表
        List<TrainSeatQueryResp> list = BeanUtil.copyToList(trainSeatList, TrainSeatQueryResp.class);

        // 构建并返回分页响应对象
        PageResp<TrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**
     * 删除指定ID的火车座位
     *
     * @param id 要删除的火车座位的主键ID
     */
    public void delete(Long id) {
        // 调用mapper删除指定ID的火车座位
        trainSeatMapper.deleteByPrimaryKey(id);
    }


    /**
     * 生成指定列车车次的座位信息。
     * @param trainCode 列车车次代码。
     */
    @Transactional
    public void genTrainSeat(String trainCode) {
        DateTime now = DateTime.now();

        // 清空当前车次下的所有座位记录
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        trainSeatMapper.deleteByExample(trainSeatExample);

        // 查询当前车次下的所有车厢信息
        List<TrainCarriage> carriageList = trainCarriageService.selectByTrainCode(trainCode);
        LOG.info("当前车次下的车厢数：{}", carriageList.size());

        // 遍历每个车厢，生成座位信息
        for (TrainCarriage trainCarriage : carriageList) {
            // 获取车厢座位布局信息：行数和座位类型
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            int seatIndex = 1;

            // 根据座位类型确定座位列数
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(seatType);
            LOG.info("根据车厢的座位类型，筛选出所有的列：{}", colEnumList);

            // 遍历每行，再遍历每列，生成每个座位并保存到数据库
            for (int row = 1; row <= rowCount; row++) {
                for (SeatColEnum seatColEnum : colEnumList) {
                    // 构建座位对象并保存
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(SnowUtils.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(row), '0', 2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(now);
                    trainSeat.setUpdateTime(now);
                    trainSeatMapper.insert(trainSeat);
                }
            }
        }
    }

    /**
     * 根据火车编码查询座位信息。
     *
     * @param trainCode 火车编码，用于查询特定火车的座位信息。
     * @return 返回一个火车座位信息的列表，按座位ID升序排列。
     */
    public List<TrainSeat> selectByTrainCode(String trainCode) {
        // 创建TrainSeatExample对象用于设定查询条件和排序方式
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.setOrderByClause("`id` asc"); // 设定查询结果按座位ID升序排列

        // 创建查询条件
        TrainSeatExample.Criteria criteria = trainSeatExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode); // 设定查询条件为火车编码等于传入的trainCode

        // 执行查询并返回结果
        return trainSeatMapper.selectByExample(trainSeatExample);
    }


}