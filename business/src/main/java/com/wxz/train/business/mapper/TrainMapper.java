package com.wxz.train.business.mapper;

import com.wxz.train.business.domain.Train;
import com.wxz.train.business.domain.TrainExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TrainMapper接口定义了与火车相关数据的操作方法。
 */
public interface TrainMapper {
    /**
     * 根据例子查询记录数量。
     *
     * @param example 用于查询条件的示例对象
     * @return 符合条件的记录数量
     */
    long countByExample(TrainExample example);

    /**
     * 根据例子删除记录。
     *
     * @param example 用于删除条件的示例对象
     * @return 影响的记录数
     */
    int deleteByExample(TrainExample example);

    /**
     * 根据主键删除记录。
     *
     * @param id 要删除的记录的主键
     * @return 影响的记录数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条记录。
     *
     * @param record 要插入的记录对象
     * @return 影响的记录数
     */
    int insert(Train record);

    /**
     * 选择性插入一条记录。
     *
     * @param record 要插入的记录对象
     * @return 影响的记录数
     */
    int insertSelective(Train record);

    /**
     * 根据例子查询记录列表。
     *
     * @param example 用于查询条件的示例对象
     * @return 符合条件的记录列表
     */
    List<Train> selectByExample(TrainExample example);

    /**
     * 根据主键查询一条记录。
     *
     * @param id 要查询的记录的主键
     * @return 查询到的记录对象
     */
    Train selectByPrimaryKey(Long id);

    /**
     * 根据例子选择性更新记录。
     *
     * @param record 要更新的记录对象
     * @param example 用于更新条件的示例对象
     * @return 影响的记录数
     */
    int updateByExampleSelective(@Param("record") Train record, @Param("example") TrainExample example);

    /**
     * 根据例子更新记录。
     *
     * @param record 要更新的记录对象
     * @param example 用于更新条件的示例对象
     * @return 影响的记录数
     */
    int updateByExample(@Param("record") Train record, @Param("example") TrainExample example);

    /**
     * 根据主键选择性更新记录。
     *
     * @param record 要更新的记录对象
     * @return 影响的记录数
     */
    int updateByPrimaryKeySelective(Train record);

    /**
     * 根据主键更新记录。
     *
     * @param record 要更新的记录对象
     * @return 影响的记录数
     */
    int updateByPrimaryKey(Train record);
}
