package com.changgou.order.controller;

import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author LiXiang
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    @GetMapping("/addCart")
    public Result addCart(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num, @RequestParam(value = "type",required = false,defaultValue = "0") Integer type) {
        //动态获取登录人信息

        String username = tokenDecode.getUserInfo().get("username");
        cartService.addCart(skuId,num,username,type);
        return R.T("添加购物车");
    }

    @GetMapping("/list")
    public Map list(){
        String username = tokenDecode.getUserInfo().get("username");
        return cartService.list(username);
    }

    @GetMapping("/getUsername")
    public Result getUsername() {
        String username = tokenDecode.getUserInfo().get("username");
        return R.T("查询",username);
    }
}
