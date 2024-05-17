package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.Train;
import com.wxz.train.business.domain.TrainExample;
import com.wxz.train.business.mapper.TrainMapper;
import com.wxz.train.business.req.TrainQueryReq;
import com.wxz.train.business.req.TrainSaveReq;
import com.wxz.train.business.resp.TrainQueryResp;
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
public class TrainService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);

    @Resource
    private TrainMapper trainMapper;

    /**
     * 保存或更新火车信息
     *
     * @param req 包含火车信息的请求对象，类型为 TrainSaveReq
     * 该方法首先会根据 TrainSaveReq 对象中的信息创建一个 Train 对象。
     * 如果 Train 对象的 id 为空，则认为是新记录，需要进行唯一性校验，
     * 若校验通过，则生成唯一 id 并保存到数据库中。
     * 如果 Train 对象的 id 不为空，则认为是更新记录，直接更新数据库中的相应记录。
     */
    public void save(TrainSaveReq req) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        if (ObjectUtil.isNull(train.getId())) {

            // 在保存新记录之前，校验火车代码的唯一性
            Train trainDB = selectByUnique(req.getCode());
            if (ObjectUtil.isNotEmpty(trainDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }

            train.setId(SnowUtils.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        } else {
            // 更新记录时，只更新更新时间
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }
    }

    /**
     * 根据唯一标识码选择火车。
     *
     * @param code 火车的唯一标识码。
     * @return 如果找到匹配的火车，则返回该火车对象；如果没有找到，则返回null。
     */
    private Train selectByUnique(String code) {
        // 创建火车样本对象，并基于代码设置查询条件
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria()
                .andCodeEqualTo(code);
        // 根据条件查询火车
        List<Train> list = trainMapper.selectByExample(trainExample);
        // 如果查询结果非空，返回第一个火车对象；否则返回null
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询火车列表
     *
     * @param req 包含查询条件、分页信息的请求对象
     * @return 返回包含查询结果和总条数的分页响应对象
     */
    public PageResp<TrainQueryResp> queryList(TrainQueryReq req) {
        // 创建火车实体例子并设置排序条件
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("id desc");
        TrainExample.Criteria criteria = trainExample.createCriteria();

        // 日志记录查询的页码和每页条数
        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        // 使用PageHelper进行分页，开始查询指定页码和条数的培训列表
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Train> trainList = trainMapper.selectByExample(trainExample);

        // 使用PageInfo包装查询结果，获取分页信息
        PageInfo<Train> pageInfo = new PageInfo<>(trainList);
        // 日志记录总行数和总页数
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 将查询结果的实体列表转换为响应实体列表
        List<TrainQueryResp> list = BeanUtil.copyToList(trainList, TrainQueryResp.class);

        // 创建并设置分页响应对象，包括总条数和结果列表
        PageResp<TrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }


    /**
     * 根据ID删除火车记录
     * @param id 火车记录的唯一标识符
     */
    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询所有火车记录
     * 该方法不接受任何参数，调用后会查询数据库中所有的火车记录，并将这些记录转换为TrainQueryResp类型的列表返回。
     *
     * @return 返回火车记录列表，列表中的每个元素都是TrainQueryResp类型的。这个列表包含了数据库中所有的火车记录，记录的顺序按照火车编号（code）升序排列。
     */
    public List<TrainQueryResp> queryAll() {
        // 从数据库中查询所有火车记录
        List<Train> trainList = selectAll();
        // 将查询到的火车记录转换为TrainQueryResp类型的列表并返回
        return BeanUtil.copyToList(trainList, TrainQueryResp.class);
    }

    /**
     * 从数据库中查询所有火车记录
     * 该方法内部创建了一个TrainExample对象，并设置了查询条件为按照火车编号升序排序，然后调用trainMapper的selectByExample方法进行查询。
     *
     * @return 返回火车记录列表，列表中的每个元素都是Train类型，包含了数据库中所有的火车记录，记录的顺序按照火车编号（code）升序排列。
     */
    public List<Train> selectAll() {
        // 创建查询条件，设置排序方式为火车编号升序
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code asc");
        // 根据查询条件查询所有火车记录
        return trainMapper.selectByExample(trainExample);
    }

}