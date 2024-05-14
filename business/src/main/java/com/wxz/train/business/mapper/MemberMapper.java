package com.wxz.train.business.mapper;

import com.wxz.train.business.domain.Member;
import com.wxz.train.business.domain.MemberExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MemberMapper接口定义了成员信息的数据库操作方法。
 */
public interface MemberMapper {
    /**
     * 根据条件统计成员数量。
     *
     * @param example 包含查询条件的MemberExample对象
     * @return 符合条件的成员数量
     */
    long countByExample(MemberExample example);

    /**
     * 根据条件删除成员信息。
     *
     * @param example 包含删除条件的MemberExample对象
     * @return 删除的记录数
     */
    int deleteByExample(MemberExample example);

    /**
     * 根据主键删除成员信息。
     *
     * @param id 成员的主键ID
     * @return 删除的记录数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一个新的成员信息。
     *
     * @param record 包含成员信息的Member对象
     * @return 插入的记录数
     */
    int insert(Member record);

    /**
     * 选择性插入成员信息。
     *
     * @param record 包含成员信息的Member对象
     * @return 插入的记录数
     */
    int insertSelective(Member record);

    /**
     * 根据条件查询成员信息列表。
     *
     * @param example 包含查询条件的MemberExample对象
     * @return 成员信息列表
     */
    List<Member> selectByExample(MemberExample example);

    /**
     * 根据主键查询成员信息。
     *
     * @param id 成员的主键ID
     * @return 对应的成员信息
     */
    Member selectByPrimaryKey(Long id);

    /**
     * 根据条件选择性更新成员信息。
     *
     * @param record 包含更新内容的Member对象
     * @param example 包含更新条件的MemberExample对象
     * @return 更新的记录数
     */
    int updateByExampleSelective(@Param("record") Member record, @Param("example") MemberExample example);

    /**
     * 根据条件更新成员信息。
     *
     * @param record 包含更新内容的Member对象
     * @param example 包含更新条件的MemberExample对象
     * @return 更新的记录数
     */
    int updateByExample(@Param("record") Member record, @Param("example") MemberExample example);

    /**
     * 根据主键选择性更新成员信息。
     *
     * @param record 包含更新内容的Member对象
     * @return 更新的记录数
     */
    int updateByPrimaryKeySelective(Member record);

    /**
     * 根据主键更新成员信息。
     *
     * @param record 包含更新内容的Member对象
     * @return 更新的记录数
     */
    int updateByPrimaryKey(Member record);
}
