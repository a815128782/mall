package com.changgou.web.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * @author LiXiang
 */
@Service
public class AuthService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 从cookie中获取jti的值
     * @param request
     * @return
     */
    public String getJtiFromCookie(ServerHttpRequest request) {
        HttpCookie httpCookie = request.getCookies().getFirst("uid");
        if(httpCookie != null) {
            String jti = httpCookie.getValue();
            return jti;
        }
        return null;
    }

    /**
     * 从Redis中根据jti 获取相对应的 jwt
     * @param jti
     * @return
     */
    public String getJwtFromRedis(String jti) {
        String jwt = stringRedisTemplate.boundValueOps(jti).get();
        return jwt;
    }
}
