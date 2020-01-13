package com.changgou.seckill.controller;

import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.seckill.config.TokenDecode;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiXiang
 */
@RestController
@RequestMapping("/seckillorder")
public class SeckillOrderController {

    @Autowired
    TokenDecode tokenDecode;
    @Autowired
    SeckillOrderService seckillOrderService;

    @RequestMapping("/add")
    public Result add(@RequestParam("time") String time,@RequestParam("id") Long id) {
        //1.动态获取到当前登录人
        String username = tokenDecode.getUserInfo().get("username");

        //2.基于业务层进行秒杀下单
        boolean result = seckillOrderService.add(id, time, username);
        //3.返回结果
        if(result){
            //下单成功
            return R.T("下单");
        }else{
            //下单失败
            return R.F("下单");
        }

    }
}
