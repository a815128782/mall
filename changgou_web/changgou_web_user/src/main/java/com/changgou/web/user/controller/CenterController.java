package com.changgou.web.user.controller;

import com.changgou.common.entity.Result;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.Center;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wcenter")
public class CenterController {
    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    //查询个人信息
    @GetMapping("/findCenter")
    @ResponseBody
    public Result findCenter() {
        Result result = userFeign.findCenter();
        return result;
    }

    @GetMapping("/findCitiesList")
    @ResponseBody
    public Result findCitiesList(@RequestParam("province") String province){
        Result result = userFeign.findCitiesList(province);
        return result;
    }

    @GetMapping("/findAreasList")
    @ResponseBody
    public Result findAreasList(@RequestParam("city") String city){
        Result result = userFeign.findAreasList(city);
        return result;
    }

    @PutMapping("/updateCenter")
    @ResponseBody
    public Result updateCenter(@RequestBody Center center){
        Result result = userFeign.updateCenter(center);
        return result;
    }

    @GetMapping("/toCenterInfo")
    public String toCenterInfo() {
        return "center-setting-info";
    }

}
