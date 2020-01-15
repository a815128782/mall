package com.changgou.search.controller;

import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.user.UserCode;
import com.changgou.common.util.SMSUtils;
import com.changgou.common.util.ValidateCodeUtils;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author LiXiang
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    public static final String VALIDATECODE="validateCode_";

    @Autowired
    UserFeign userFeign;
    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping
    public String toRegistry(){
        return "register";
    }

    @PostMapping("/user/add")
    @ResponseBody
    public Result addU(@RequestParam("smsCode")String smsCode,@RequestBody User user) {
        if(StringUtils.isEmpty(smsCode)){
            ExceptionCast.cast(UserCode.USER_VALIDATECODE_ERROR);
        }
        String username = user.getUsername();
        String phone = user.getPhone();
        String password = user.getPassword();
        return userFeign.add(smsCode,user);
    }

    @RequestMapping("/user/send")
    @ResponseBody
    public Result send(@RequestParam("phone")String phone){
        //随机生成4位数验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        try {
            System.out.println(SMSUtils.VALIDATE_CODE);
            System.out.println(phone);
            System.out.println("" + validateCode);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, phone, "" + validateCode);
        }catch (Exception e){
            e.printStackTrace();
            return R.F("验证码发送");
        }
        redisTemplate.boundValueOps(VALIDATECODE+phone).set(validateCode,300, TimeUnit.SECONDS);
        return R.T("验证码发送");
    }
}
