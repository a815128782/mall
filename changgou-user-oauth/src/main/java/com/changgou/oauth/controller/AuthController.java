package com.changgou.oauth.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.system.SystemCode;
import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.CookieUtil;
import com.changgou.oauth.util.TokenDecode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author LiXiang
 */
@Controller
@CrossOrigin
@RequestMapping("/oauth")
public class AuthController {

    String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvFsEiaLvij9C1Mz+oyAmt47whAaRkRu/8kePM+X8760UGU0RMwGti6Z9y3LQ0RvK6I0brXmbGB/RsN38PVnhcP8ZfxGUH26kX0RK+tlrxcrG+HkPYOH4XPAL8Q1lu1n9x3tLcIPxq8ZZtuIyKYEmoLKyMsvTviG5flTpDprT25unWgE4md1kthRWXOnfWHATVY7Y/r4obiOL1mS5bEa/iNKotQNnvIAKtjBM4RlIDWMa6dmz+lHtLtqDD2LF1qwoiSIHI75LQZ/CNYaHCfZSxtOydpNKq8eb1/PGiLNolD4La2zf0/1dlcr5mkesV570NxRmU1tFm8Zd3MZlZmyv9QIDAQAB-----END PUBLIC KEY-----";

    @Autowired
    private AuthService authService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    TokenDecode tokenDecode;

    @Value("${auth.clientId}")
    private String clientID;
    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${auth.cookieDomain}")
    private String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    private Integer cookieMaxAge;

    @RequestMapping("/toLogin")
    public String toLogin(@RequestParam(value = "FROM",required = false,defaultValue = "") String from, Model model) {
        model.addAttribute("from",from);
        return "login";
    }


    @RequestMapping("/login")
    @ResponseBody
    //  登录校验
    public Result login(String username, String password, HttpServletResponse response){
        //校验参数
        if(StringUtils.isEmpty(username)) {
            ExceptionCast.cast(SystemCode.SYSTEM_LOGIN_USERNAME_ERROR);
        }
        if(StringUtils.isEmpty(password)){
            ExceptionCast.cast(SystemCode.SYSTEM_LOGIN_PASSWORD_ERROR);
        }
        //申请令牌 authtoken
        AuthToken authToken = authService.login(username, password, clientID, clientSecret);
        if(authToken == null) {
            ExceptionCast.cast(SystemCode.SYSTEM_LOGIN_ERROR);
        }
        //将jti中的值存入Cookie中
        this.saveJtiToCookie(authToken.getJti(),response);
        String jwt = authToken.getAccessToken();
        Jwt token = JwtHelper.decodeAndVerify(jwt,new RsaVerifier(publicKey));
        String claims = token.getClaims();
        Map map = JSON.parseObject(claims, Map.class);
        String username1 = (String) map.get("username");
        CookieUtil.addCookie(response,cookieDomain,"/","username",username1,cookieMaxAge,false);


        //返回结果
        return R.T("登录",authToken.getJti());
    }

    /**
     * 将令牌的短标识存入Cookie的方法
     * @param jti
     * @param response
     */
    private void saveJtiToCookie(String jti, HttpServletResponse response) {
        CookieUtil.addCookie(response,cookieDomain,"/","uid",jti,cookieMaxAge,false);
    }

    @RequestMapping("/getUsername")
    @ResponseBody
    public Result getUsername(@RequestParam("jti")String jti) {
        String jwt = (String) stringRedisTemplate.boundValueOps(jti).get();
        Jwt token = JwtHelper.decodeAndVerify(jwt,new RsaVerifier(publicKey));
        String claims = token.getClaims();
        Map map = JSON.parseObject(claims, Map.class);
        String username = (String) map.get("username");
//        TokenDecode tokenDecode = new TokenDecode();
//        String username = tokenDecode.getUserInfo().get("username");
        return R.T("查询",username);
    }

}
