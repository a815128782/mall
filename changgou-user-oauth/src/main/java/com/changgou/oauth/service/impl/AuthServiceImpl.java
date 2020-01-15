package com.changgou.oauth.service.impl;

import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.user.UserCode;
import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author LiXiang
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;//Redis模板类

    @Value("${auth.ttl}")//过期时间
    private long ttl;


    @Override
    public AuthToken login(String username, String password, String clientId, String secret) {
        //1.申请令牌
        //从Eureka中通过服务名称获取路径和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        URI uri = serviceInstance.getUri();
        //2.拼接路径
        String url = uri+"/oauth/token";

        //第二部:封装请求参数,以什么方式登录
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password"); //oauth 密码模式登陆
        body.add("username",username);
        body.add("password",password);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization",this.getHttpBasic(clientId,secret));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(body,headers);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        //第三步:发送请求
        /**
         * 参数1:请求的地址
         * 参数2:请求的方式
         * 参数3:封装当前请求的参数
         * 参数4:数据返回的类型
         */
        ResponseEntity<Map> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        } catch (RestClientException e) {
            ExceptionCast.cast(UserCode.USER_LOGIN_ERROR);
        }
        Map map = responseEntity.getBody();
        if(map == null ||map.get("access_token") == null ||map.get("refresh_token") == null||map.get("jti") == null){
            ExceptionCast.cast(UserCode.USER_LOGIN_ERROR);
        }
        //2.封装结果数据
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken((String) map.get("access_token"));
        authToken.setRefreshToken((String) map.get("refresh_token"));
        authToken.setJti((String) map.get("jti"));


        //3.将 jti 作为Redis中的key,将 jwt 作为Redis中的value进行数据的存放
        stringRedisTemplate.boundValueOps(authToken.getJti()).set(authToken.getAccessToken(),ttl, TimeUnit.SECONDS);
        return authToken;
    }

    private String getHttpBasic(String clientId, String secret) {
        String value = clientId+":"+secret;
        byte[] encode = Base64Utils.encode(value.getBytes());
        return "Basic "+new String(encode);
    }
}