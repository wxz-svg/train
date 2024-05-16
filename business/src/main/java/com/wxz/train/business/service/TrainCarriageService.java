package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.TrainCarriage;
import com.wxz.train.business.domain.TrainCarriageExample;
import com.wxz.train.business.enums.SeatColEnum;
import com.wxz.train.business.mapper.TrainCarriageMapper;
import com.wxz.train.business.req.TrainCarriageQueryReq;
import com.wxz.train.business.req.TrainCarriageSaveReq;
import com.wxz.train.business.resp.TrainCarriageQueryResp;
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
public class TrainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainCarriageService.class);

    @Resource
    private TrainCarriageMapper trainCarriageMapper;

    /**
     * 保存火车车厢信息。
     *
     * @param req 包含车厢信息的请求对象，其中应包括车厢的座位类型、行列数等必要信息。
     *            该方法将根据请求对象中的信息，自动计算出列数和总座位数，并保存或更新数据库中的火车车厢信息。
     */
    public void save(TrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        // 计算列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getColCount() * req.getRowCount());

        TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);
        if (ObjectUtil.isNull(trainCarriage.getId())) {

            // 新增车厢前的唯一键校验
            TrainCarriage trainCarriageDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(trainCarriageDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
            }

            trainCarriage.setId(SnowUtils.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.insert(trainCarriage);
        } else {
            // 更新现有车厢信息
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.updateByPrimaryKey(trainCarriage);
        }
    }

    /**
     * 根据唯一的火车代码和车厢索引选择火车车厢。
     *
     * @param trainCode 火车代码，用于识别特定的火车。
     * @param index 车厢索引，用于识别火车上的特定车厢。
     * @return 如果找到匹配的火车车厢，则返回该车厢对象；如果没有找到，则返回null。
     */
    private TrainCarriage selectByUnique(String trainCode, Integer index) {
        // 创建火车车厢示例用于查询条件构建
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        // 根据构建的查询条件，查询符合条件的所有火车车厢
        List<TrainCarriage> list = trainCarriageMapper.selectByExample(trainCarriageExample);
        // 如果查询结果非空，返回第一个元素，否则返回null
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询火车车厢信息列表
     *
     * @param req 包含查询条件、分页信息的请求对象
     * @return 返回火车车厢信息的分页响应对象
     */
    public PageResp<TrainCarriageQueryResp> queryList(TrainCarriageQueryReq req) {
        // 初始化火车车厢查询样例，设置排序条件
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("train_code asc, `index` asc");

        // 创建查询条件
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        // 如果有提供火车编码，则添加到查询条件中
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        // 记录查询的页码和每页条数
        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        // 使用PageHelper进行分页，调用mapper查询符合条件的火车车厢列表
        PageHelper.startPage(req.getPage(), req.getSize());
        List<TrainCarriage> trainCarriageList = trainCarriageMapper.selectByExample(trainCarriageExample);

        // 使用PageInfo获取分页信息
        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriageList);
        // 记录总行数和总页数
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 利用BeanUtil将查询结果转换为前端需要的响应对象列表
        List<TrainCarriageQueryResp> list = BeanUtil.copyToList(trainCarriageList, TrainCarriageQueryResp.class);

        // 构建分页响应对象
        PageResp<TrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }


    /**
     * 根据ID删除列车车厢信息
     * @param id 列车车厢的唯一标识符
     */
    public void delete(Long id) {
        trainCarriageMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据列车编号查询所有车厢信息，并按车厢序号升序排序
     * @param trainCode 列车的编号
     * @return 返回匹配的列车车厢信息列表
     */
    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        // 创建查询条件，按车厢序号升序排序，并指定查询条件为特定的列车编号
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("`index` asc");
        TrainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();
        criteria.andTrainCodeEqualTo(trainCode);
        // 执行查询并返回结果
        return trainCarriageMapper.selectByExample(trainCarriageExample);
    }

}