package com.changgou.web.order.controller;

import com.changgou.common.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.pay.feign.PayFeign;
import com.changgou.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author LiXiang
 */
@Controller
@RequestMapping("/wxpay")
public class PayController {

    @Autowired
    PayFeign payFeign;
    @Autowired
    OrderFeign orderFeign;
    @Autowired
    UserFeign userFeign;

    /**
     * 跳转到微信支付的二维码页面
     */
    @GetMapping
    public String wxPay(String orderId,Model model){
        //1.根据orderId查询订单,如果订单不存在,跳转到错误页面
        Order order = orderFeign.findById(orderId).getData();
        if(order == null) {
//            model.addAttribute("msg","");
            return "fail";
        }
        //2.根据订单的支付状态进行判断,如果不是未支付的订单,跳转到错误页面
        if(!"0".equals(order.getPayStatus())) {
            //            model.addAttribute("msg","");
            return "fail";
        }
        //3.基于payFeign调用统一下单的接口,并获取返回结果
        Result payResult = payFeign.nativePay(orderId, String.valueOf(order.getPayMoney()));
        if(payResult.getData() == null) {
//            model.addAttribute("msg","");
            return "fail";
        }
        Map payMap = (Map) payResult.getData();
        model.addAllAttributes(payMap);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        //4.封装结果
        return "wxpay";
    }

    @GetMapping("/zfb")
    public String zfbPay(String orderId,Model model){
        Order order = orderFeign.findById(orderId).getData();
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        if(order == null) {
//            model.addAttribute("msg","");
            model.addAttribute("username",username);
            return "fail";
        }
        Result payResult=payFeign.createAlipayNative(orderId,String.valueOf(order.getPayMoney()));
        if(payResult.getData() == null) {
//            model.addAttribute("msg","");
            model.addAttribute("username",username);
            return "fail";
        }
        Map payMap = (Map) payResult.getData();
        model.addAllAttributes(payMap);
        model.addAttribute("username",username);
        return "zfbpay";
    }



    @RequestMapping("/toPaySuccess")
    public String toPaySuccess(Integer payMoney,Model model) {
        model.addAttribute("payMoney",payMoney);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "paysuccess";
    }

    @RequestMapping("/toSuccess")
    public String toSuccess(Integer payMoney,String orderId,Model model) {
        model.addAttribute("orderId",orderId);
        model.addAttribute("payMoney",payMoney);
        return "success";
    }

}

