package com.wxz.train.business.mapper;

import com.wxz.train.business.domain.TrainSeat;
import com.wxz.train.business.domain.TrainSeatExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TrainSeatMapper接口定义了对火车座位表进行CRUD操作的方法
 */
public interface TrainSeatMapper {
    /**
     * 根据条件查询座位数量
     * @param example 查询条件实例
     * @return 符合条件的座位数量
     */
    long countByExample(TrainSeatExample example);

    /**
     * 根据条件删除座位记录
     * @param example 查询条件实例
     * @return 删除的记录数
     */
    int deleteByExample(TrainSeatExample example);

    /**
     * 根据主键删除座位记录
     * @param id 主键ID
     * @return 删除的记录数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一个新的座位记录
     * @param record 座位记录实例
     * @return 插入的记录数
     */
    int insert(TrainSeat record);

    /**
     * 选择性插入一个新的座位记录
     * @param record 座位记录实例
     * @return 插入的记录数
     */
    int insertSelective(TrainSeat record);

    /**
     * 根据条件查询座位记录列表
     * @param example 查询条件实例
     * @return 符合条件的座位记录列表
     */
    List<TrainSeat> selectByExample(TrainSeatExample example);

    /**
     * 根据主键查询座位记录
     * @param id 主键ID
     * @return 对应的座位记录
     */
    TrainSeat selectByPrimaryKey(Long id);

    /**
     * 根据条件选择性更新座位记录
     * @param record 座位记录实例，包含更新值
     * @param example 查询条件实例
     * @return 更新的记录数
     */
    int updateByExampleSelective(@Param("record") TrainSeat record, @Param("example") TrainSeatExample example);

    /**
     * 根据条件更新座位记录
     * @param record 座位记录实例，包含更新值
     * @param example 查询条件实例
     * @return 更新的记录数
     */
    int updateByExample(@Param("record") TrainSeat record, @Param("example") TrainSeatExample example);

    /**
     * 根据主键选择性更新座位记录
     * @param record 座位记录实例，包含更新值
     * @return 更新的记录数
     */
    int updateByPrimaryKeySelective(TrainSeat record);

    /**
     * 根据主键更新座位记录
     * @param record 座位记录实例，包含更新值
     * @return 更新的记录数
     */
    int updateByPrimaryKey(TrainSeat record);
}
