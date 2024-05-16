package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.Station;
import com.wxz.train.business.domain.StationExample;
import com.wxz.train.business.mapper.StationMapper;
import com.wxz.train.business.req.StationQueryReq;
import com.wxz.train.business.req.StationSaveReq;
import com.wxz.train.business.resp.StationQueryResp;
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
public class StationService {

    private static final Logger LOG = LoggerFactory.getLogger(StationService.class);

    @Resource
    private StationMapper stationMapper;

    /**
     * 保存或更新车站信息
     *
     * @param req 包含车站信息的请求对象，类型为 StationSaveReq
     * 该方法首先会根据请求对象创建一个 Station 实例，如果实例的 id 为空，则认为是新记录，
     * 需要进行唯一性校验并插入；如果 id 不为空，则认为是更新记录，直接更新。
     */
    public void save(StationSaveReq req) {
        DateTime now = DateTime.now();
        Station station = BeanUtil.copyProperties(req, Station.class);
        if (ObjectUtil.isNull(station.getId())) {

            // 在保存新记录之前，校验车站名称的唯一性
            Station stationDB = selectByUnique(req.getName());
            if (ObjectUtil.isNotEmpty(stationDB)) {
                // 如果发现名称重复，则抛出业务异常
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_STATION_NAME_UNIQUE_ERROR);
            }

            // 生成唯一ID，设置创建时间和更新时间，然后插入到数据库
            station.setId(SnowUtils.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            stationMapper.insert(station);
        } else {
            // 如果是更新记录，则只更新更新时间，并保存到数据库
            station.setUpdateTime(now);
            stationMapper.updateByPrimaryKey(station);
        }
    }

    /**
     * 根据名称选择唯一的站台。
     *
     * @param name 站台的名称，作为查询的依据。
     * @return 如果找到了具有指定名称的站台，则返回该站台对象；如果没有找到，则返回null。
     */
    private Station selectByUnique(String name) {
        // 创建站台查询示例，并设置查询条件为名称等于传入的参数值
        StationExample stationExample = new StationExample();
        stationExample.createCriteria().andNameEqualTo(name);

        // 根据查询条件查询站台信息
        List<Station> list = stationMapper.selectByExample(stationExample);

        // 如果查询结果不为空，返回查询结果的第一个人；否则返回null
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询加油站列表
     *
     * @param req 包含查询条件、分页信息的请求对象
     * @return 返回带有查询结果和分页信息的响应对象
     */
    public PageResp<StationQueryResp> queryList(StationQueryReq req) {
        // 初始化数据库查询条件
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("id desc");
        StationExample.Criteria criteria = stationExample.createCriteria();

        // 记录查询的页码和每页条数
        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());

        // 使用PageHelper进行分页，开始查询
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Station> stationList = stationMapper.selectByExample(stationExample);

        // 获取分页信息
        PageInfo<Station> pageInfo = new PageInfo<>(stationList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 将查询结果转换为响应对象列表
        List<StationQueryResp> list = BeanUtil.copyToList(stationList, StationQueryResp.class);

        // 构建并返回分页响应对象
        PageResp<StationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }


    /**
     * 根据ID删除车站信息
     * @param id 车站的唯一标识符
     */
    public void delete(Long id) {
        stationMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询所有车站信息，并按照拼音升序排序
     * @return 返回车站信息的查询结果列表，每个车站信息被封装成StationQueryResp对象
     */
    public List<StationQueryResp> queryAll() {
        // 创建查询条件，设置按照拼音升序排序
        StationExample stationExample = new StationExample();
        stationExample.setOrderByClause("name_pinyin asc");
        // 根据查询条件查询所有车站信息
        List<Station> stationList = stationMapper.selectByExample(stationExample);
        // 将查询结果转换为StationQueryResp对象的列表后返回
        return BeanUtil.copyToList(stationList, StationQueryResp.class);
    }

}