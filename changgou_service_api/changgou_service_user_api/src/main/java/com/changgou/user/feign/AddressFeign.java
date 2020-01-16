package com.changgou.user.feign;

import com.changgou.common.entity.Result;
import com.changgou.user.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LiXiang
 */
@FeignClient(name = "user")
public interface AddressFeign {

    @GetMapping("/address/list")
    public Result<List<Address>> list();

    @GetMapping("/address")
    public Result findAll();

    @PostMapping("/address")
    public Result add(@RequestBody Address address);

    @GetMapping("/address/{id}")
    public Result findById(@PathVariable("id") Integer id);

    @PutMapping(value="/address/{id}")
    public Result update(@RequestBody Address address,@PathVariable("id") Integer id);

    @DeleteMapping(value = "/address/{id}" )
    public Result delete(@PathVariable("id") Integer id);


    @GetMapping("/address/findAddressByUsername")
    public Result findAddressByUsername();


    @PutMapping(value="/address/updateIsDefault/{id}")
    public Result updateIsDefault(@PathVariable("id") Integer id);
}
