package com.wxz.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.wxz.train.common.context.LoginMemberContext;
import com.wxz.train.common.utils.SnowUtils;
import com.wxz.train.member.domain.Passenger;
import com.wxz.train.member.domain.PassengerExample;
import com.wxz.train.member.mapper.PassengerMapper;
import com.wxz.train.member.req.PassengerQueryReq;
import com.wxz.train.member.req.PassengerSaveReq;
import com.wxz.train.member.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // 为乘客生成唯一的会员ID和乘客ID
        passenger.setMemberId(LoginMemberContext.getId());
        passenger.setId(SnowUtils.getSnowflakeNextId()); // 生成唯一的乘客ID
        passenger.setCreateTime(now); // 设置创建时间
        passenger.setUpdateTime(now); // 设置更新时间
        passengerMapper.insert(passenger); // 将乘客信息插入数据库
    }

    /**
     * 查询乘客信息列表
     *
     * @param req 查询请求对象，可能包含乘客的memberId等过滤条件
     * @return 返回乘客信息的查询结果列表，每个乘客信息以PassengerQueryResp对象形式返回
     */
    public List<PassengerQueryResp> queryList(PassengerQueryReq req) {
        // 创建乘客信息查询的示例对象，并设置查询条件
        PassengerExample passengerExample = new PassengerExample();
        PassengerExample.Criteria criteria = passengerExample.createCriteria();

        // 如果请求中包含memberId，则添加memberId的查询条件
        if (ObjectUtil.isNotNull(req.getMemberId())) {
            criteria.andMemberIdEqualTo(req.getMemberId());
        }

        // 根据条件查询乘客信息列表
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);

        // 将查询到的乘客信息列表转换为PassengerQueryResp对象列表后返回
        return BeanUtil.copyToList(passengerList, PassengerQueryResp.class);
    }


}
