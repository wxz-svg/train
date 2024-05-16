package com.wxz.train.batch.mapper;

import com.wxz.train.batch.domain.Member;
import com.wxz.train.batch.domain.MemberExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MemberMapper接口定义了对会员表进行CRUD操作的方法。
 */
public interface MemberMapper {
    /**
     * 根据条件统计表中记录数。
     *
     * @param example 包含查询条件的对象
     * @return 表中符合条件的记录数
     */
    long countByExample(MemberExample example);

    /**
     * 根据条件删除记录。
     *
     * @param example 包含删除条件的对象
     * @return 删除的记录数
     */
    int deleteByExample(MemberExample example);

    /**
     * 根据主键删除记录。
     *
     * @param id 要删除的记录的主键
     * @return 删除的记录数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条新记录。
     *
     * @param record 要插入的数据对象
     * @return 插入的记录数
     */
    int insert(Member record);

    /**
     * 选择性插入一条新记录。
     *
     * @param record 要插入的数据对象
     * @return 插入的记录数
     */
    int insertSelective(Member record);

    /**
     * 根据条件查询记录。
     *
     * @param example 包含查询条件的对象
     * @return 包含查询结果的列表
     */
    List<Member> selectByExample(MemberExample example);

    /**
     * 根据主键查询一条记录。
     *
     * @param id 要查询的记录的主键
     * @return 查询到的记录对象
     */
    Member selectByPrimaryKey(Long id);

    /**
     * 根据条件选择性更新记录。
     *
     * @param record 要更新的数据对象
     * @param example 包含更新条件的对象
     * @return 更新的记录数
     */
    int updateByExampleSelective(@Param("record") Member record, @Param("example") MemberExample example);

    /**
     * 根据条件更新记录。
     *
     * @param record 要更新的数据对象
     * @param example 包含更新条件的对象
     * @return 更新的记录数
     */
    int updateByExample(@Param("record") Member record, @Param("example") MemberExample example);

    /**
     * 根据主键选择性更新记录。
     *
     * @param record 要更新的数据对象
     * @return 更新的记录数
     */
    int updateByPrimaryKeySelective(Member record);

    /**
     * 根据主键更新记录。
     *
     * @param record 要更新的数据对象
     * @return 更新的记录数
     */
    int updateByPrimaryKey(Member record);
}
