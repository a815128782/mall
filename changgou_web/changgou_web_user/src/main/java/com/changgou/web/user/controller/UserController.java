package com.changgou.web.user.controller;

import com.changgou.common.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.Vo;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.user.UserCode;
import com.changgou.common.util.CookieUtil;
import com.changgou.common.util.SMSUtils;
import com.changgou.common.util.ValidateCodeUtils;
import com.changgou.order.feign.CartFeign;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author LiXiang
 */
@Controller
@RequestMapping("/wuser")
public class UserController {

    @Autowired
    private OrderFeign orderFeign;


    @RequestMapping("/user")
    public String userCenter(Model model){
        List<Vo> voList = orderFeign.findOrderByUserName().getData();
        model.addAttribute( "voList",voList);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "center-index";
    }

    @RequestMapping("/toCenterIndex")
    public String toCenterIndex(Model model) {
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "center-index";
    }

    @RequestMapping("/toPay")
    public String toPay(@RequestParam("orderId")String orderId,@RequestParam("money")String money, Model model){
        model.addAttribute("orderId",orderId);
        model.addAttribute("money",money);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "pay";
    }


    public static final String VALIDATECODE="validateCode_";

    @Autowired
    UserFeign userFeign;
    @Autowired
    CartFeign cartFeign;
    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/index")
    public String toIndex(Model model, HttpServletRequest request) {
        Map<String, String> map = CookieUtil.readCookie(request, "username");
        String username = map.get("username");
        model.addAttribute("username",username);
        return "index";
    }

    @RequestMapping("/register")
    public String toRegistry(Model model){
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "register";
    }

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestParam("smsCode")String smsCode, @RequestBody User user) {
        if(StringUtils.isEmpty(smsCode)){
            ExceptionCast.cast(UserCode.USER_VALIDATECODE_ERROR);
        }
        String username = user.getUsername();
        String phone = user.getPhone();
        String password = user.getPassword();
        return userFeign.add(smsCode,user);
    }

    @RequestMapping("/send")
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

    @GetMapping("/getUsername")
    @ResponseBody
    public Result getUsername(){
        String username = (String) cartFeign.getUsername().getData();
        return R.T("查询",username);
    }

    @GetMapping("/exit")
    public String exit(HttpServletResponse response) {
        CookieUtil.addCookie(response,"localhost","/","username","123",0,false);
        CookieUtil.addCookie(response,"localhost","/","uid","123",0,false);
        return "index";
    }

    @GetMapping("/toPage/{spuId}")
    public String toPage(@PathVariable("spuId")String spuId){
        return spuId;
    }

}
