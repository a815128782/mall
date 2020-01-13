package com.changgou.seckill.web.controller;

import com.changgou.common.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiXiang
 */
@RestController
@RequestMapping("/wseckillorder")
public class SeckillOrderController {

    @RequestMapping("/add")
    public Result add(@RequestParam("time") String time, @RequestParam("id") Long id){


        return null;
    }
}
