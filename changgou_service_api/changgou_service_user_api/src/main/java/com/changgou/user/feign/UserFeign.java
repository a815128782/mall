package com.changgou.user.feign;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Sku;
import com.changgou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author LiXiang
 */
@FeignClient(name = "user")
public interface UserFeign {

    @GetMapping("/user/load/{username}")
    public User findUserInfo(@PathVariable("username")String username);


   /* @GetMapping("/user/decr/userPoints")
    public Result decrUserPoints(@RequestParam String username, @RequestParam Integer points);*/
    @PostMapping("/user/collect/add/{id}")
    public Result add(@PathVariable("id") String id);

    @PostMapping("/user/collect/addFootMark/{id}")
    public Result addFootMark(@PathVariable("id") String id);

    @GetMapping("/user/collect/list")
    public List<Sku> list();

    @GetMapping("/user/collect/list2FootMark")
    public List<Sku> list2FootMark();


    /***
     * 新增数据
     * @param user
     * @return smsCode
     */
//    @PostMapping("/user")
//    public Result add(@RequestParam("smsCode") String smsCode,@RequestBody User user);


    @PostMapping("/user/add")
    public Result add(@RequestParam("smsCode") String smsCode,@RequestBody User user);

    @PostMapping("/user/addUser2")
    public Result addUser2(@RequestParam("smsCode") String smsCode);

    @GetMapping("/user/getUsername")
    public Result getUsername();

    /*@GetMapping("/user/decr/userPoints")
    public Result decrUserPoints(@RequestParam String username, @RequestParam Integer points);*/
}
