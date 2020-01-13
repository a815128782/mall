package com.changgou.system.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author LiXiang
 */
@Component
public class UrlFilter implements GlobalFilter, Ordered {

    /**
     * 具体业务逻辑
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取客户端的访问IP
        System.out.println("经过了第" + getOrder() + "个过滤器");
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        System.out.println("path: " + path);//客户端的访问路径
        //放行
        return chain.filter(exchange);
    }

    /**
     * 过滤器的执行优先级
     *
     * @return 返回值越小, 执行优先级越高
     */
    @Override
    public int getOrder() {
        return 2;
    }

}

