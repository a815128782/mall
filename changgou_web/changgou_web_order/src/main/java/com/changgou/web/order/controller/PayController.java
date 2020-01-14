package com.changgou.web.order.controller;

import com.changgou.common.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.pay.feign.PayFeign;
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
        //4.封装结果
        return "wxpay";
    }

    @GetMapping("/zfb")
    public String zfbPay(String orderId,Model model){
        Order order = orderFeign.findById(orderId).getData();
        if(order == null) {
//            model.addAttribute("msg","");
            return "fail";
        }
        Result payResult=payFeign.createAlipayNative(orderId,String.valueOf(order.getPayMoney()));
        if(payResult.getData() == null) {
//            model.addAttribute("msg","");
            return "fail";
        }
        Map payMap = (Map) payResult.getData();
        model.addAllAttributes(payMap);
        return "zfbpay";
    }

    //支付宝的回调
    @RequestMapping("/alipayCallBack")
    public String alipayCallBack(HttpServletRequest request,Model model){
        System.out.println("===================");
        System.out.println(request.getParameterMap());
        Map<String,String> params = new HashMap();
        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){

                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }
        if("TRADE_SUCCESS".equals(params.get("trade_status"))) {
            System.out.println("支付成功");
            return "paysuccess";
           // orderFeign.updateOrderStatus(params.get("out_trade_no")+"", params.get("trade_no"));//修改订单状态
        }
       return null;
    }

    @RequestMapping("/toPaySuccess")
    public String toPaySuccess(Integer payMoney,Model model) {
        model.addAttribute("payMoney",payMoney);
        return "paysuccess";
    }

}
