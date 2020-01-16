package com.changgou.web.user.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.feign.OrderItemFeign;
import com.changgou.order.pojo.Vo;
import com.changgou.pay.feign.PayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import com.changgou.common.entity.R;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.user.UserCode;
import com.changgou.common.util.CookieUtil;
import com.changgou.common.util.SMSUtils;
import com.changgou.common.util.ValidateCodeUtils;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.pojo.OrderList;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private OrderItemFeign orderItemFeign;
    @Autowired
    private PayFeign payFeign;
    @Autowired
    UserFeign userFeign;
    @Autowired
    CartFeign cartFeign;


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

    //根据用户名查询已发货订单
    @RequestMapping("/receive")
    public String toPay(Model model){
        String username = (String) userFeign.getUsername().getData();
//        String username = "heima";

        model.addAttribute("username",username);
        List<Vo> voList = orderFeign.findConsignByUsername(username).getData();
        model.addAttribute("volist",voList);

        return "center-order-receive";
    }


    public static final String VALIDATECODE="validateCode_";




    @Autowired
    RedisTemplate redisTemplate;



  /*  @RequestMapping("/user")
    public String userCenter(Model model){

        List<Vo> voList = orderFeign.findOrderByUserName().getData();

        model.addAttribute( "voList",voList);

        return "center-index";
    }*/

    @RequestMapping("/pay")
    private String undaip(Model model){
        List<OrderList> orderLists = orderFeign.myOrder().getData();
        model.addAttribute("orderLists",orderLists);
//        List<Vo> voList = orderFeign.findOrderByUserName().getData();
//
//        model.addAttribute( "voList",voList);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "center-order-pay";
    }
    @RequestMapping("/sendt")
    private String receive(Model model){
        List<OrderList> orderLists = orderFeign.myOrder().getData();
        model.addAttribute("orderLists",orderLists);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "center-order-send";
    }

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

    //根据orderId查询订单详情
    @GetMapping("/findOrderItemById")
    public Result findOrderById(String orderId){
        Result result = orderItemFeign.findById(orderId);
        return new Result(true, StatusCode.OK,"查询商品订单详情成功",result);
    }

    @GetMapping("/myOrder/{orderId}")
    public String myOrder(Model model,@PathVariable("orderId") String orderId){
        model.addAttribute("orderId",orderId);
        return "myOrder";
    }
    //支付宝回调的订单详情(未实现)
    @GetMapping("/oneOrder")
    public Result myOrder(String orderId){
        Result result = payFeign.aliqueryOrder(orderId);
        Map map = (Map) result.getData();
        return new Result(true,StatusCode.OK,"查询支付宝回调信息成功",map);
    }

    @GetMapping("/toPage/{spuId}")
    public String toPage(@PathVariable("spuId")String spuId,Model model){
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return spuId;
    }

}
