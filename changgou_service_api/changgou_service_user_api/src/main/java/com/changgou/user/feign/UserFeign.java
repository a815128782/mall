package com.changgou.user.feign;

import com.changgou.common.entity.Result;
import com.changgou.user.pojo.Center;
import com.changgou.goods.pojo.Sku;
import com.changgou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiXiang
 */
@FeignClient(name = "user")
public interface UserFeign {

    @GetMapping("/user/load/{username}")
    public User findUserInfo(@PathVariable("username")String username);

    @GetMapping("/user/decr/userPoints")
    public Result decrUserPoints(@RequestParam("username") String username, @RequestParam("points") Integer points);

    @GetMapping("/user/center")
    public Result findCenter();


   /* @GetMapping("/user/decr/userPoints")
    public Result decrUserPoints(@RequestParam String username, @RequestParam Integer points);*/
    @PostMapping("/user/collect/add/{id}")
    public Result add(@PathVariable("id") String id);

    @GetMapping("/user/collect/deleteCollect/{id}")
    public Result deleteCollect(@PathVariable("id") String id);

    @PostMapping("/user/collect/addFootMark/{id}")
    public Result addFootMark(@PathVariable("id") String id);

    @GetMapping("/user/collect/list")
    public Result<List<Sku>> list();

    @GetMapping("/user/collect/list2FootMark")
    public Result<List<Sku>> list2FootMark();

    @GetMapping("/user/collect/deleteFootMark")
    Result deleteFootMark(@PathVariable("id") String id);


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

    @GetMapping("/user/findCitiesList")
    public Result findCitiesList(@RequestParam("province") String province);

    @GetMapping("/user/findAreasList")
    public Result findAreasList(@RequestParam("city") String city);

    @PutMapping("/user/updateCenter")
    public Result updateCenter(@RequestBody Center center);

    @GetMapping("/user/findProvincesList")
    public Result findProvincesList();

    @GetMapping("/user/findUser")
    public Result<User> findUser();



    @GetMapping("/user/updateUser")
    public Result updateUser(@RequestParam("username")String username, @RequestParam("password")String password);


    /*@GetMapping("/user/decr/userPoints")
    public Result decrUserPoints(@RequestParam String username, @RequestParam Integer points);*/
}
