package com.changgou.user.feign;

import com.changgou.common.entity.Result;
import com.changgou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author LiXiang
 */
@FeignClient(name = "user")
public interface UserFeign {

    @GetMapping("/user/load/{username}")
    public User findUserInfo(@PathVariable("username")String username);

    /*@GetMapping("/user/decr/userPoints")
    public Result decrUserPoints(@RequestParam String username, @RequestParam Integer points);*/
}
