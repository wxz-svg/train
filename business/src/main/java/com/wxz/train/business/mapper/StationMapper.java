package com.wxz.train.business.mapper;

import com.wxz.train.business.domain.Station;
import com.wxz.train.business.domain.StationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * StationMapper接口定义了与数据库进行站台信息交互的方法
 */
public interface StationMapper {
    /**
     * 根据条件统计站台数量
     * @param example 查询条件实例
     * @return 符合条件的站台数量
     */
    long countByExample(StationExample example);

    /**
     * 根据条件删除站台信息
     * @param example 查询条件实例
     * @return 影响的记录数
     */
    int deleteByExample(StationExample example);

    /**
     * 根据主键删除站台信息
     * @param id 站台主键
     * @return 影响的记录数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一个新的站台信息
     * @param record 站台信息记录
     * @return 影响的记录数
     */
    int insert(Station record);

    /**
     * 选择性插入一个新的站台信息
     * @param record 站台信息记录
     * @return 影响的记录数
     */
    int insertSelective(Station record);

    /**
     * 根据条件查询站台信息列表
     * @param example 查询条件实例
     * @return 站台信息列表
     */
    List<Station> selectByExample(StationExample example);

    /**
     * 根据主键查询站台信息
     * @param id 站台主键
     * @return 站台信息记录
     */
    Station selectByPrimaryKey(Long id);

    /**
     * 根据条件选择性更新站台信息
     * @param record 站台信息记录
     * @param example 查询条件实例
     * @return 影响的记录数
     */
    int updateByExampleSelective(@Param("record") Station record, @Param("example") StationExample example);

    /**
     * 根据条件更新站台信息
     * @param record 站台信息记录
     * @param example 查询条件实例
     * @return 影响的记录数
     */
    int updateByExample(@Param("record") Station record, @Param("example") StationExample example);

    /**
     * 根据主键选择性更新站台信息
     * @param record 站台信息记录
     * @return 影响的记录数
     */
    int updateByPrimaryKeySelective(Station record);

    /**
     * 根据主键更新站台信息
     * @param record 站台信息记录
     * @return 影响的记录数
     */
    int updateByPrimaryKey(Station record);
}
