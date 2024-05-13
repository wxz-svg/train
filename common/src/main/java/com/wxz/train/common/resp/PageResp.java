package com.wxz.train.common.resp;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应类，用于包装分页数据的响应信息。
 * @param <T> 泛型，代表列表中元素的类型。
 */
public class PageResp<T> implements Serializable {

    /**
     * 总条数：表示查询结果的总数量。
     */
    private Long total;

    /**
     * 当前页的列表：包含当前页返回的数据项列表。
     */
    private List<T> list;

    /**
     * 获取总条数。
     * @return 返回查询结果的总数量。
     */
    public Long getTotal() {
        return total;
    }

    /**
     * 设置总条数。
     * @param total 要设置的查询结果总数量。
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * 获取当前页的列表。
     * @return 返回当前页的数据项列表。
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 设置当前页的列表。
     * @param list 要设置的当前页数据项列表。
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    /**
     * 重写toString方法，用于方便地打印PageResp对象的内容。
     * @return 返回关于PageResp对象的字符串表示，包括总条数和当前页列表的简要信息。
     */
    @Override
    public String toString() {
        return "PageResp{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}

