package com.wxz.train.gateway.config;

import com.wxz.train.gateway.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoginMemberFilter implements Ordered, GlobalFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberFilter.class);

    /**
     * 对通过网关的请求进行过滤，实现登录验证的功能。
     * 如果请求路径属于特定无需登录验证的路径，则直接放行。
     * 否则，检查请求头中的token，如果不存在或无效，则拦截请求并返回未授权状态。
     *
     * @param exchange 服务器网络交换机，用于获取和响应请求
     * @param chain 网关过滤器链，用于继续处理请求或拦截请求
     * @return Mono<Void> 表示异步操作完成，不返回任何结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 判断请求路径是否属于无需登录验证的路径，若是则直接放行
        if (path.contains("/admin")
                || path.contains("/hello")
                || path.contains("/member/member/login")
                || path.contains("/member/member/send-code")) {
            LOG.info("不需要登录验证：{}", path);
            return chain.filter(exchange);
        } else {
            LOG.info("需要登录验证：{}", path);
        }

        // 从请求头中获取token
        String token = exchange.getRequest().getHeaders().getFirst("token");
        LOG.info("会员登录验证开始，token：{}", token);
        if (token == null || token.isEmpty()) {
            // 如果token为空，则拦截请求，返回未授权状态
            LOG.info("token为空，请求被拦截");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 校验token的有效性
        boolean validate = JwtUtils.validate(token);
        if (validate) {
            // 如果token有效，则放行请求
            LOG.info("token有效，放行该请求");
            return chain.filter(exchange);
        } else {
            // 如果token无效，则拦截请求，返回未授权状态
            LOG.warn("token无效，请求被拦截");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

    }

    /**
     * 设置当前对象的优先级。该方法重写了父类的getOrder方法。
     * 在这个上下文中，优先级的数值越小，表示优先级越高。
     *
     * @return int 返回优先级的整数值。在这个实现中，始终返回0，表示最高优先级。
     */
    @Override
    public int getOrder() {
        return 0;
    }

}