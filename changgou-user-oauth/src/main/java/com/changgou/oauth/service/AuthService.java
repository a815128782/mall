package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

/**
 * @author LiXiang
 */
public interface AuthService {
    AuthToken login(String username,String password,String clientId,String secret);
}
