package com.wxz.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxz.train.common.context.LoginMemberContext;
import com.wxz.train.common.resp.PageResp;
import com.wxz.train.common.utils.SnowUtils;
import com.wxz.train.member.domain.Passenger;
import com.wxz.train.member.domain.PassengerExample;
import com.wxz.train.member.mapper.PassengerMapper;
import com.wxz.train.member.req.PassengerQueryReq;
import com.wxz.train.member.req.PassengerSaveReq;
import com.wxz.train.member.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    private static final Logger log = LoggerFactory.getLogger(PassengerService.class);

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
     *         返回的结果包含分页信息，如总条数、总页数等
     */
    public PageResp<PassengerQueryResp> queryList(PassengerQueryReq req) {
        // 创建乘客信息查询的示例对象，并设置查询条件
        PassengerExample passengerExample = new PassengerExample();
        PassengerExample.Criteria criteria = passengerExample.createCriteria();

        // 根据请求中的memberId添加查询条件，如果memberId存在的话
        if (ObjectUtil.isNotNull(req.getMemberId())) {
            criteria.andMemberIdEqualTo(req.getMemberId());
        }

        // 使用MyBatis分页插件进行分页查询，根据请求的页码和每页显示的条数进行设置
        PageHelper.startPage(req.getPage(), req.getSize());

        // 执行查询操作，获取乘客信息列表
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);

        // 使用PageInfo对查询结果进行包装，获取分页相关信息
        PageInfo<Passenger> pageInfo = new PageInfo<>(passengerList);

        // 打印分页信息日志
        log.info("总行数: {}", pageInfo.getTotal());
        log.info("总页数: {}", pageInfo.getPages());

        // 将乘客信息实体列表转换为业务对象列表，以供前端使用
        List<PassengerQueryResp> list = BeanUtil.copyToList(passengerList, PassengerQueryResp.class);

        // 构建并返回分页响应对象
        PageResp<PassengerQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }



}
