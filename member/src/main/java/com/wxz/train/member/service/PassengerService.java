package com.wxz.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.wxz.train.common.utils.SnowUtils;
import com.wxz.train.member.domain.Passenger;
import com.wxz.train.member.mapper.PassengerMapper;
import com.wxz.train.member.req.PassengerSaveReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    /**
     * 保存乘客信息
     *
     * @param req 乘客信息请求对象，包含需要保存的乘客信息
     * 无返回值
     */
    public void save(PassengerSaveReq req) {
        // 获取当前时间
        DateTime now = DateTime.now();
        // 使用BeanUtil工具类将请求对象复制到Passenger实体对象中
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        // 为乘客对象生成唯一的ID
        passenger.setId(SnowUtils.getSnowflakeNextId());
        // 设置创建时间和更新时间为当前时间
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        // 将乘客对象插入到数据库中
        passengerMapper.insert(passenger);
    }
}
