package com.wxz.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.wxz.train.common.context.LoginMemberContext;
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
     * @param req 乘客信息请求对象，包含需要保存的乘客信息
     * 该方法不返回任何内容
     */
    public void save(PassengerSaveReq req){
        DateTime now = DateTime.now(); // 获取当前时间
        Passenger passenger = BeanUtil.copyProperties(req,Passenger.class); // 将请求对象的属性复制到Passenger实体中

        // 判断乘客ID是否为空，若为空则进行赋值并插入新乘客信息；否则更新乘客信息
        if (ObjectUtil.isNull(passenger.getId())) {
            // 为乘客生成唯一的会员ID和乘客ID
            passenger.setMemberId(LoginMemberContext.getId());
            passenger.setId(SnowUtils.getSnowflakeNextId()); // 生成唯一的乘客ID
            passenger.setCreateTime(now); // 设置创建时间
            passenger.setUpdateTime(now); // 设置更新时间
            passengerMapper.insert(passenger); // 将乘客信息插入数据库
        } else {
            // 更新乘客信息
            passenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKey(passenger);
        }
    }
}
