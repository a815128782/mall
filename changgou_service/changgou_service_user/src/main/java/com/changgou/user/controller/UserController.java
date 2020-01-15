package com.changgou.user.controller;
import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.system.SystemCode;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.pojo.Areas;
import com.changgou.user.pojo.Center;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.pojo.Cities;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    TokenDecode tokenDecode;

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('seckill_list')")
//    @PreAuthorize("hasAuthority('seckill_list')")
    public Result findAll(){
        List<User> userList = userService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",userList) ;
    }

    /***
     * 根据ID查询数据
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public Result findById(@PathVariable String username){
        User user = userService.findById(username);
        return new Result(true,StatusCode.OK,"查询成功",user);
    }

    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username")String username) {
        if(username == null) {
            ExceptionCast.cast(SystemCode.SYSTEM_LOGIN_USERNAME_ERROR);
        }
        User user = userService.findById(username);
        return user;
    }


    /***
     * 新增数据
     * @param user
     * @return
     */
   /* @PostMapping
    public Result add(@RequestParam("smsCode")String smsCode,@RequestBody User user){
        userService.add(smsCode,user);
        return new Result(true,StatusCode.OK,"添加成功");
    }*/

    @PostMapping("/add")
    public Result add(@RequestParam("smsCode") String smsCode,@RequestBody User user){

//        user.setUsername(username);
//        user.setPassword(password);
//        user.setPhone(phone);
        userService.add(smsCode,user);
        return new Result(true,StatusCode.OK,"注册成功");
    }

    @PostMapping("/addUser2")
    public Result addUser2(@RequestParam("smsCode") String smsCode){

        return R.T("132");
    }


    /***
     * 修改数据
     * @param user
     * @param username
     * @return
     */
    @PutMapping(value="/{username}")
    public Result update(@RequestBody User user,@PathVariable String username){
        user.setUsername(username);
        userService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param username
     * @return
     */
    @DeleteMapping(value = "/{username}" )
    public Result delete(@PathVariable String username){
        userService.delete(username);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search" )
    public Result findList(@RequestBody Map searchMap){
        List<User> list = userService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestBody Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<User> pageList = userService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }

    /*@GetMapping("/decr/userPoints")
    public Result decrUserPoints(@RequestParam String username,@RequestParam Integer points){
        userService.decrUserPoints(username,points);
        return R.T("积分添加");
    }*/


    @GetMapping("/center")
    public Result findCenter(){
        String username = tokenDecode.getUserInfo().get("username");
        Center center = userService.findCenter(username);
        return new Result(true,StatusCode.OK,"查询成功",center);
    }

    @GetMapping("/findCitiesList")
    public Result findCitiesList(@RequestParam("province") String province){
        List<Cities> citiesList = userService.findCitiesList(province);
        return new Result(true,StatusCode.OK,"查询成功",citiesList);
    }

    @GetMapping("/findAreasList")
    public Result findAreasList(@RequestParam("city") String city){
        List<Areas> areasList = userService.findAreasList(city);
        return new Result(true,StatusCode.OK,"查询成功",areasList);
    }

    @PutMapping("/updateCenter")
    public Result updateCenter(@RequestBody Center center) {
        Integer integer = userService.updateCenter(center);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    @GetMapping("/getUsername")
    public Result getUsername(){
        String username = tokenDecode.getUserInfo().get("username");
        return R.T("查询",username);
    }

}


