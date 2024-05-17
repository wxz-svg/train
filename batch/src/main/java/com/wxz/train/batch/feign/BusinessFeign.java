package com.wxz.train.batch.feign;

import com.wxz.train.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

/**
 * BusinessFeign接口，用于通过Feign进行远程调用。
 * @FeignClient注解指定了调用的服务名称为"business"。
 * 可以通过配置url属性来指定调用的URL，但当前配置中未启用。
 */
// @FeignClient("business")
@FeignClient(name = "business", url = "http://127.0.0.1:8002/business")
public interface BusinessFeign {

    /**
     * 调用业务服务的hello方法。
     * @return 返回一个字符串类型的hello消息。
     */
    @GetMapping("/hello")
    String hello();

    /**
     * 根据指定日期生成车次信息列表。
     *
     * @param date 生成报告的日期，格式为"yyyy-MM-dd"。
     * @return 返回一个通用响应对象，包含生成的报告或者错误信息。
     */
    @GetMapping("/admin/daily-train/gen-daily/{date}")
    public CommonResp<Object> genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
