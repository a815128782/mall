package com.changgou.web.gateway.filter;

import com.changgou.web.gateway.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author LiXiang
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final String LOGIN_URL="http://localhost:8001/api/oauth/toLogin";

    @Autowired
    private AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "application/json");
        //1.判断当前请求是否为登录请求，如果是则直接放行
        String path = request.getURI().getPath();
        URI uri = request.getURI();
        if("/api/oauth/login".equals(path)||!UrlFilter.hasAuthorize(path)){
            //直接放行
            return chain.filter(exchange);
        }

        //2.从cookie中获取jti的值，如果改制不存在，拒绝本次访问
        String jti = authService.getJtiFromCookie(request);
        if(StringUtils.isEmpty(jti)) {
            //拒绝访问
            /*DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(JSONArray.toJSONString(new ExceptionCatch().
                    customException(new CustomException(GatewayCode.GATEWAY_AUTH_LOGIN_ERROR))).getBytes());
            return response.writeWith(Flux.just(dataBuffer));*/
            //跳转登录页面
            Mono<Void> voidMono = this.toLoginPage(LOGIN_URL + "?FROM=" + uri, exchange);
            return voidMono;
        }
        //3.从redis中获取jwt的值,若果改制不存在,拒接本次访问
        String jwt = authService.getJwtFromRedis(jti);
        if(StringUtils.isEmpty(jwt)) {
            /*DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(JSONArray.toJSONString(new ExceptionCatch().
                    customException(new CustomException(GatewayCode.GATEWAY_TOKEN_ERROR))).getBytes());
            return response.writeWith(Flux.just(dataBuffer));*/

            return this.toLoginPage(LOGIN_URL,exchange);
        }
        //4.对当前的请求对象进行增强,让它携带令牌的信息

        /**
         * 将获得令牌 通过 header 向下传递
         */
        request.mutate().header("Authorization","Bearer "+jwt);

        return chain.filter(exchange);
    }

    /**
     * 跳转登录页面
     * @param loginUrl
     * @param exchange
     * @return
     */
    private Mono<Void> toLoginPage(String loginUrl, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location",loginUrl);
        return response.setComplete();
    }

    /**
     * 设置过滤器执行优先级
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
