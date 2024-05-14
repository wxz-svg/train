package com.wxz.train.business.mapper;

import com.wxz.train.business.domain.TrainStation;
import com.wxz.train.business.domain.TrainStationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TrainStationMapper接口定义了与火车站信息相关的数据库操作。
 */
public interface TrainStationMapper {
    /**
     * 根据例子查询火车站数量。
     *
     * @param example 用于查询条件的实例，可选字段用于筛选结果。
     * @return 返回匹配查询条件的火车站数量。
     */
    long countByExample(TrainStationExample example);

    /**
     * 根据例子删除火车站记录。
     *
     * @param example 用于删除条件的实例，可选字段用于确定删除的记录。
     * @return 返回删除的记录数。
     */
    int deleteByExample(TrainStationExample example);

    /**
     * 根据主键删除火车站记录。
     *
     * @param id 火车站的唯一标识符。
     * @return 返回删除的记录数。
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一个新的火车站记录。
     *
     * @param record 火车站信息的实例。
     * @return 返回插入的记录数。
     */
    int insert(TrainStation record);

    /**
     * 选择性插入一个新的火车站记录。只会插入非null的字段。
     *
     * @param record 火车站信息的实例。
     * @return 返回插入的记录数。
     */
    int insertSelective(TrainStation record);

    /**
     * 根据例子查询火车站记录。
     *
     * @param example 用于查询条件的实例，可选字段用于筛选结果。
     * @return 返回匹配查询条件的火车站记录列表。
     */
    List<TrainStation> selectByExample(TrainStationExample example);

    /**
     * 根据主键查询火车站记录。
     *
     * @param id 火车站的唯一标识符。
     * @return 返回匹配指定主键的火车站记录。
     */
    TrainStation selectByPrimaryKey(Long id);

    /**
     * 根据例子选择性更新火车站记录。
     *
     * @param record 包含更新内容的火车站信息实例。
     * @param example 用于更新条件的实例，可选字段用于确定更新的记录。
     * @return 返回更新的记录数。
     */
    int updateByExampleSelective(@Param("record") TrainStation record, @Param("example") TrainStationExample example);

    /**
     * 根据例子更新火车站记录。
     *
     * @param record 包含更新内容的火车站信息实例。
     * @param example 用于更新条件的实例，可选字段用于确定更新的记录。
     * @return 返回更新的记录数。
     */
    int updateByExample(@Param("record") TrainStation record, @Param("example") TrainStationExample example);

    /**
     * 根据主键选择性更新火车站记录。
     *
     * @param record 包含更新内容的火车站信息实例，只会更新非null字段。
     * @return 返回更新的记录数。
     */
    int updateByPrimaryKeySelective(TrainStation record);

    /**
     * 根据主键更新火车站记录。
     *
     * @param record 包含更新内容的火车站信息实例。
     * @return 返回更新的记录数。
     */
    int updateByPrimaryKey(TrainStation record);
}
