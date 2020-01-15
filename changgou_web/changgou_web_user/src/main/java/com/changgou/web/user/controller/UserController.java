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

@Controller
@RequestMapping("/wuser")
public class UserController {

    @Autowired
    private OrderFeign orderFeign;


    @RequestMapping("/user")
    public String userCenter(Model model){

        List<Vo> voList = orderFeign.findOrderByUserName().getData();

        model.addAttribute( "voList",voList);

        return "center-index";
    }





}
