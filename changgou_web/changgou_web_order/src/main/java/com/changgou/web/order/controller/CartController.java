package com.changgou.web.order.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.order.feign.CartFeign;
import com.changgou.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author LiXiang
 */
@Controller
@RequestMapping("/wcart")
public class CartController {

    @Autowired
    CartFeign cartFeign;
    @Autowired
    SkuFeign skuFeign;
    @Autowired
    UserFeign userFeign;


    //查询购物车
    @GetMapping("/list")
    public String list(Model model) {
        Map map = cartFeign.list();
        model.addAttribute("items",map);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "cart";
    }

    //添加购物车
    @GetMapping("/addCart/{id}")
    public String addCart(@PathVariable("id") String id,@RequestParam("num") Integer num,Integer type, Model model) {
        cartFeign.addCart(id, num,type);
        Sku sku = skuFeign.findById(id).getData();
        Map map = JSON.parseObject(sku.getSpec(), Map.class);
        String color = map.get("颜色").toString();
        model.addAttribute("sku",sku);
        model.addAttribute("num",num);
        model.addAttribute("color",color);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "success-cart";
    }


    //添加购物车
    @GetMapping("/add")
    @ResponseBody
    public Result<Map> add(String id,Integer num,Integer type) {
        cartFeign.addCart(id, num,type);
        Map map = cartFeign.list();
        return R.T("添加购物车",map);
    }
}
