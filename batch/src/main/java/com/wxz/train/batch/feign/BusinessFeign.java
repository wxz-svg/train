package com.wxz.train.batch.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * BusinessFeign接口，用于通过Feign进行远程调用。
 * @FeignClient注解指定了调用的服务名称为"business"。
 * 可以通过配置url属性来指定调用的URL，但当前配置中未启用。
 */
@FeignClient(name = "business", url = "http://127.0.0.1:8002/business")
// @FeignClient("business")
public interface BusinessFeign {

    /**
     * 调用业务服务的hello方法。
     * @return 返回一个字符串类型的hello消息。
     */
    @GetMapping("/hello")
    String hello();
}
