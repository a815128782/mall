package com.changgou.oauth.controller;

import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.system.SystemCode;
import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author LiXiang
 */
@Controller
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private AuthService authService;

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
}
