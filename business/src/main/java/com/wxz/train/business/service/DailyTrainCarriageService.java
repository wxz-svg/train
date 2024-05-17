package com.wxz.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.business.domain.DailyTrainCarriage;
import com.wxz.train.business.domain.DailyTrainCarriageExample;
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

import java.util.List;

@Service
public class DailyTrainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainCarriageService.class);

    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

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


    public void delete(Long id) {
        dailyTrainCarriageMapper.deleteByPrimaryKey(id);
    }
}