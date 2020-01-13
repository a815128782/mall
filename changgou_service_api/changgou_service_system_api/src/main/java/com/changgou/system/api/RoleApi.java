package com.changgou.system.api;

import com.changgou.common.entity.Result;
import com.changgou.system.pojo.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @Description:
 * @Version: V1.0
 */
@Api(value="角色管理接口",description = "角色管理接口，提供页面的增、删、改、查")
public interface RoleApi {


    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有角色列表")
    Result findAll();

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询角色数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "角色ID",required=true,paramType="path",dataType="int"),
    })
    Result findById(Integer id);


    /***
     * 新增数据
     * @param role
     * @return
     */
    @ApiOperation("新增角色")
    Result add(Role role);


    /***
     * 修改数据
     * @param role
     * @param id
     * @return
     */
    @ApiOperation("修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "角色ID",required=true, paramType = "path", dataTypeClass = Integer.class),
    })
    Result update(Role role, Integer id);


    /***
     * 根据ID删除角色数据
     * @param id
     * @return
     */
    @ApiOperation("删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "角色ID",required=true,paramType="path",dataType="int"),
    })
    Result delete(Integer id);

    /***
     * 多条件搜索角色数据
     * @param searchMap
     * @return
     */
    @ApiOperation("查询角色列表")
    Result findList(Map searchMap);


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="searchMap",value = "条件查询 JSON数据， 角色名称或者是角色首字母"),
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message="Indicates ..."),
//            @ApiResponse(code = 404, message = "not found error")
//    })
    Result findPage(Map searchMap, Integer page, Integer size);

}
