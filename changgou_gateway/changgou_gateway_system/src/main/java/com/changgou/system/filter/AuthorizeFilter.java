package com.changgou.system.filter;

import com.alibaba.fastjson.JSONArray;
import com.changgou.common.exception.CustomException;
import com.changgou.common.exception.ExceptionCatch;
import com.changgou.common.model.response.gateway.GatewayCode;
import com.changgou.system.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZE_TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1. 获取请求
        ServerHttpRequest request = exchange.getRequest();
        //2. 获取响应
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("Content-Type", "application/json");
        //3. 如果是登录请求则放行
        if (request.getURI().getPath().contains("/admin/login")) {
            return chain.filter(exchange);
        }
        //4. 获取请求头
        HttpHeaders headers = request.getHeaders();
        //5. 请求头中获取令牌
        String token = headers.getFirst(AUTHORIZE_TOKEN);
        // 6. 判断请求头中是否有令牌
        if (StringUtils.isEmpty(token)) {
            // 7. 响应中放入返回的状态吗, 没有权限访问
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //8. 返回
//            return response.setComplete();
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(JSONArray.toJSONString(new ExceptionCatch().
                    customException(new CustomException(GatewayCode.GATEWAY_TOKEN_ERROR))).getBytes());
            return response.writeWith(Flux.just(dataBuffer));

        }
        //9. 如果请求头中有令牌则解析令牌
        try {
            Claims claims = JwtUtil.parseJWT(token);
            log.info("claims: {} ", claims);
            List<String> roles = claims.get(JwtUtil.JWT_CLAIMS, List.class);
            if(roles==null || roles.size()==0 || !"admin".equals(roles.get(0))){
                DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(JSONArray.toJSONString
                        (new ExceptionCatch().customException(new CustomException(GatewayCode.GATEWAY_AUTHORIZE_ERROR))).getBytes());
                return response.writeWith(Flux.just(dataBuffer));
            }
        } catch (Exception e) {
//            e.printStackTrace();
            //10. 解析jwt令牌出错, 说明令牌过期或者伪造等不合法情况出现
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //11. 返回
//            return response.setComplete();
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(JSONArray.toJSONString(
                    new ExceptionCatch().customException(new CustomException(GatewayCode.GATEWAY_TOKEN_ERROR))).getBytes());
            return response.writeWith(Flux.just(dataBuffer));
        }
        //12. 放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

