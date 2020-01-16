package com.changgou.web.user.controller;

import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.Center;
import com.changgou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
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

    @GetMapping("/findProvincesList")
    @ResponseBody
    public Result findProvincesList(){
        Result result = userFeign.findProvincesList();
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
    public String toCenterInfo(Model model) {
        model.addAttribute("username","heima");
        return "center-setting-info";
    }

    @GetMapping("/getUsername")
    @ResponseBody
    public Result getUsername(Model model) {
        return userFeign.findUser();
    }

    @GetMapping("/toSafe")
    public String toSafe(Model model) {
        User user = userFeign.findUser().getData();
        model.addAttribute("username",user.getUsername());
        return "center-setting-safe";
    }

    public static final String VALIDATECODE="validateCode_";

    @GetMapping("/verification")
    @ResponseBody
    public Result verification(@RequestParam("code")Integer code,@RequestParam("phone")String phone){

        if(StringUtils.isEmpty(code+"")){
            return new Result(false, StatusCode.ERROR,"请输入验证码");
        }
        String result =  redisTemplate.boundValueOps(VALIDATECODE + phone).get()+"";
        if(result.equals(code+"")){
            return R.T("");
        }
        return new Result(false, StatusCode.ERROR,"验证码错误");
    }

    @RequestMapping("/toReset")
    public String toReset(Model model){
        User user = userFeign.findUser().getData();
        model.addAttribute("username",user.getUsername());
        return "center-setting-address-phone";
    }

}
