package com.changgou.user.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:sss
 * @Date:2020/1/13 15:27
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private CollectService collectService;

    @PostMapping("/add")
    public Result add(@RequestParam("skuId") String skuId){
        //动态获取登录人信息
        String username = tokenDecode.getUserInfo().get("username");
        collectService.add(username,skuId);
        return new Result(true, StatusCode.OK,"添加收藏成功");
    }

}
