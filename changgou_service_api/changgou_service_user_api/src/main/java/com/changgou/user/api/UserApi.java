package com.changgou.user.api;

import com.changgou.common.entity.Result;
import com.changgou.user.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value="用户管理接口",description = "用户管理接口，提供页面的增、删、改、查")
public interface UserApi {

    /**
     * 查询全部数据
     * @return
     */
    @ApiOperation("查询所有用户信息")
    public Result findAll();

    /***
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @ApiOperation("根据用户名查询用户")
    public Result findById(@PathVariable String username);


    /***
     * 新增数据
     * @param user
     * @return
     */
    @ApiOperation("添加用户")
    public Result add(@RequestBody User user);


    /***
     * 修改数据
     * @param user
     * @param username
     * @return
     */
    @ApiOperation("根据用户名修改用户")
    public Result update(@RequestBody User user,@PathVariable String username);


    /***
     * 根据用户名删除品牌数据
     * @param username
     * @return
     */
    @ApiOperation("根据用户名删除用户")
    public Result delete(@PathVariable String username);

    /***
     * 多条件搜索用户
     * @param searchMap
     * @return
     */
    @ApiOperation("多条件搜索用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="searchMap",value = "条件查询 JSON数据， 用户id,账号,昵称,注册时间,不填默认查询所有"),
    })
    public Result findList(@RequestBody Map searchMap);

    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("分页搜索实现")
    @ApiImplicitParams({
            @ApiImplicitParam(name="searchMap",value = "条件查询 JSON数据， 用户id,账号,昵称,注册时间,不填默认查询所有"),
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
    public Result findPage(@RequestBody Map searchMap, @PathVariable  Integer page, @PathVariable  Integer size);


}
