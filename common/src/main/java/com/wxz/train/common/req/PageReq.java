package com.wxz.train.common.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

/**
 * 分页请求参数类，用于前端向后端发送分页查询请求时传输的参数。
 */
public class PageReq {

    /**
     * 页码，表示需要查询的页数。
     * 必填项，不能为空。
     */
    @NotNull(message = "【页码】不能为空")
    private Integer page;

    /**
     * 每页条数，表示每页返回的结果数量。
     * 必填项，不能为空且最大值不能超过100。
     */
    @NotNull(message = "【每页条数】不能为空")
    @Max(value = 100, message = "【每页条数】不能超过100")
    private Integer size;

    /**
     * 获取当前页码。
     * @return 当前页码
     */
    public Integer getPage() {
        return page;
    }

    /**
     * 设置当前页码。
     * @param page 要设置的页码
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * 获取每页条数。
     * @return 每页条数
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 设置每页条数。
     * @param size 要设置的每页条数
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * 重写toString方法，便于打印分页请求参数信息。
     * @return 分页请求参数的字符串表示
     */
    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}
