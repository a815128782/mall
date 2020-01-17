package com.changgou.web.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.common.entity.Result;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.Address;
import com.changgou.user.pojo.Center;
import com.changgou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/waddress")
public class AddressController {
    @Autowired
    private AddressFeign addressFeign;
    @Autowired
    UserFeign userFeign;

    @Autowired
    private RedisTemplate redisTemplate;



    @GetMapping("/toCenterAddress")
    public String toCenterAddress(Model model) {
        User user = userFeign.findUser().getData();
        model.addAttribute("username",user.getUsername());

        return "center-setting-address";
    }


    @GetMapping("/findAddressByUsername")
    @ResponseBody
    public Result findAddressByUsername() {
        Result result = addressFeign.findAddressByUsername();
        return result;
    }

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Address address) {
        address.setTotalAddress(address.getProvince()+address.getCity()+address.getArea()+address.getAddress());
        Result result = addressFeign.add(address);
        return result;
    }

    @GetMapping("/findById")
    @ResponseBody
    public Result findById(@RequestParam("id") Integer id) {
        Result result = addressFeign.findById(id);
        return result;
    }

    @PutMapping("/update")
    @ResponseBody
    public Result update(@RequestBody Address address) {
        address.setTotalAddress(address.getProvince()+address.getCity()+address.getArea()+address.getAddress());
        Result result = addressFeign.update(address,address.getId());
        return result;
    }

    @DeleteMapping(value = "/delete" )
    @ResponseBody
    public Result delete(@RequestParam("id") Integer id){
        Object o =  addressFeign.findById(id).getData();
        Map<String,String> map = (LinkedHashMap<String, String>) addressFeign.findById(id).getData();
        if(map.get("isDefault").equals("1")){
            List<Address> addressList = (List<Address>) addressFeign.findAddressByUsername().getData();
            String string = JSON.toJSONString(addressList.get(0));
            Address address = JSON.parseObject(string, Address.class);
            address.setIsDefault("1");
            addressFeign.update(address,address.getId());
        }
        Result result = addressFeign.delete(id);
        return result;
    }


    @PutMapping("/updateIsDefault")
    @ResponseBody
    public Result updateIsDefault(@RequestParam("id") Integer id) {
        Result result = addressFeign.updateIsDefault(id);
        return result;
    }
}
