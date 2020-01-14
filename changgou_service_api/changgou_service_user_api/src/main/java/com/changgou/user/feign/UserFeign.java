package com.changgou.user.feign;

import com.changgou.common.entity.Result;
import com.changgou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiXiang
 */
@FeignClient(name = "user")
public interface UserFeign {

    @GetMapping("/user/load/{username}")
    public User findUserInfo(@PathVariable("username")String username);

    @GetMapping("/user/decr/userPoints")
    public Result decrUserPoints(@RequestParam String username, @RequestParam Integer points);

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
