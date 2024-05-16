package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.TrainStation;
import com.wxz.train.business.domain.TrainStationExample;
import com.wxz.train.business.mapper.TrainStationMapper;
import com.wxz.train.business.req.TrainStationQueryReq;
import com.wxz.train.business.req.TrainStationSaveReq;
import com.wxz.train.business.resp.TrainStationQueryResp;
import com.wxz.train.common.enums.BusinessExceptionEnum;
import com.wxz.train.common.exception.BusinessException;
import com.wxz.train.common.resp.PageResp;
import com.wxz.train.common.utils.SnowUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainStationService.class);

    @Resource
    private TrainStationMapper trainStationMapper;

    /**
     * 保存火车站信息。
     *
     * @param req 包含火车站信息的请求对象，包括但不限于火车站名称、编号等。
     *            该方法根据请求对象中的信息，判断是新增火车站还是更新已存在的火车站信息。
     *
     * 该方法首先会校验火车站的唯一标识（火车编码和序号或火车编码和名称）是否已存在，
     * 如果不存在，则根据请求对象创建或更新火车站信息。
     */
    public void save(TrainStationSaveReq req) {
        DateTime now = DateTime.now();
        TrainStation trainStation = BeanUtil.copyProperties(req, TrainStation.class);
        if (ObjectUtil.isNull(trainStation.getId())) {

            // 校验火车站编码和序号的唯一性
            TrainStation trainStationDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }

            // 校验火车站编码和名称的唯一性
            trainStationDB = selectByUnique(req.getTrainCode(), req.getName());
            if (ObjectUtil.isNotEmpty(trainStationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR);
            }

            trainStation.setId(SnowUtils.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            trainStationMapper.insert(trainStation);
        } else {
            // 更新火车站信息
            trainStation.setUpdateTime(now);
            trainStationMapper.updateByPrimaryKey(trainStation);
        }
    }

    /**
     * 根据唯一的火车代码和索引选择火车站点。
     *
     * @param trainCode 火车代码，用于识别特定的火车线路。
     * @param index 索引，用于在火车站点列表中唯一标识一个站点。
     * @return 如果找到匹配的火车站点，则返回该站点；如果没有找到，则返回null。
     */
    private TrainStation selectByUnique(String trainCode, Integer index) {
        // 创建查询条件，指定火车代码和索引
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        // 根据查询条件查询所有匹配的火车站点
        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        // 如果存在匹配的火车站点，返回第一个站点；否则返回null
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据唯一的火车代码和站名选择火车站点。
     *
     * @param trainCode 火车代码，用于识别特定的火车线路。
     * @param name 站点名称，指定火车停靠的站点。
     * @return 如果找到匹配的火车站点，则返回该站点对象；如果没有找到，则返回null。
     */
    private TrainStation selectByUnique(String trainCode, String name) {
        // 创建查询条件，基于火车代码和站名
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andNameEqualTo(name);
        // 根据查询条件获取所有匹配的火车站点
        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        // 如果存在匹配的站点，则返回第一个匹配项，否则返回null
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询火车站点列表
     *
     * @param req 包含查询条件、页码和每页条数的请求对象
     * @return 返回火车站点查询响应对象，包含总行数和站点列表
     */
    public PageResp<TrainStationQueryResp> queryList(TrainStationQueryReq req) {
        // 初始化火车站点查询样例，设置默认排序
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("train_code asc, `index` asc");

        // 创建查询条件
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();
        // 如果有提供火车编码，则添加到查询条件中
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        // 记录查询的页码和每页条数
        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        // 使用PageHelper分页，并执行查询
        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);

        // 处理查询结果，获取分页信息
        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStationList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 将火车站点实体列表转换为查询响应实体列表
        List<TrainStationQueryResp> list = BeanUtil.copyToList(trainStationList, TrainStationQueryResp.class);

        // 构建并返回分页响应对象
        PageResp<TrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }


    public void delete(Long id) {
        trainStationMapper.deleteByPrimaryKey(id);
    }
}