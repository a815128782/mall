package com.changgou.user.controller;

import com.changgou.common.entity.Result;
import com.changgou.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:sss
 * @Date:2020/1/13 16:00
 */
@Controller
@RequestMapping("/wcollect")
public class CollectController {

    @Autowired
    private UserFeign userFeign;

    @RequestMapping("/add/{id}")
    @ResponseBody
    public Result add(@PathVariable("id") String id){
        Result result = userFeign.add(id);
        return result;
    }

}
