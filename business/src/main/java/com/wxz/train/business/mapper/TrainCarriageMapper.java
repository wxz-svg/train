package com.wxz.train.business.mapper;

import com.wxz.train.business.domain.TrainCarriage;
import com.wxz.train.business.domain.TrainCarriageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TrainCarriageMapper接口定义了火车车厢数据的CRUD操作。
 */
public interface TrainCarriageMapper {
    /**
     * 根据例子查询符合条件的火车车厢数量。
     *
     * @param example 用于查询条件的示例对象
     * @return 符合条件的火车车厢数量
     */
    long countByExample(TrainCarriageExample example);

    /**
     * 根据例子删除符合条件的所有火车车厢记录。
     *
     * @param example 用于删除条件的示例对象
     * @return 影响的记录数
     */
    int deleteByExample(TrainCarriageExample example);

    /**
     * 根据主键删除指定的火车车厢记录。
     *
     * @param id 要删除的火车车厢的主键ID
     * @return 影响的记录数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一个新的火车车厢记录。
     *
     * @param record 要插入的火车车厢对象
     * @return 影响的记录数
     */
    int insert(TrainCarriage record);

    /**
     * 选择性插入一个新的火车车厢记录。只会插入非null的字段。
     *
     * @param record 要插入的火车车厢对象
     * @return 影响的记录数
     */
    int insertSelective(TrainCarriage record);

    /**
     * 根据例子查询所有符合条件的火车车厢记录。
     *
     * @param example 用于查询条件的示例对象
     * @return 符合条件的火车车厢列表
     */
    List<TrainCarriage> selectByExample(TrainCarriageExample example);

    /**
     * 根据主键查询指定的火车车厢记录。
     *
     * @param id 要查询的火车车厢的主键ID
     * @return 指定的火车车厢对象
     */
    TrainCarriage selectByPrimaryKey(Long id);

    /**
     * 根据例子选择性更新符合条件的所有火车车厢记录。
     *
     * @param record 要更新到的火车车厢对象
     * @param example 用于更新条件的示例对象
     * @return 影响的记录数
     */
    int updateByExampleSelective(@Param("record") TrainCarriage record, @Param("example") TrainCarriageExample example);

    /**
     * 根据例子更新符合条件的所有火车车厢记录。
     *
     * @param record 要更新到的火车车厢对象
     * @param example 用于更新条件的示例对象
     * @return 影响的记录数
     */
    int updateByExample(@Param("record") TrainCarriage record, @Param("example") TrainCarriageExample example);

    /**
     * 根据主键选择性更新指定的火车车厢记录。
     *
     * @param record 要更新到的火车车厢对象
     * @return 影响的记录数
     */
    int updateByPrimaryKeySelective(TrainCarriage record);

    /**
     * 根据主键更新指定的火车车厢记录。
     *
     * @param record 要更新到的火车车厢对象
     * @return 影响的记录数
     */
    int updateByPrimaryKey(TrainCarriage record);
}
