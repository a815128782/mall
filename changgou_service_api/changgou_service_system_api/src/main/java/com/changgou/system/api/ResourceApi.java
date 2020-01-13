package com.changgou.system.api;

import com.changgou.common.entity.Result;
import com.changgou.system.pojo.Resource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @Description:
 * @Version: V1.0
 */
@Api(value="资源管理接口",description = "资源管理接口，提供页面的增、删、改、查")
public interface ResourceApi {


    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有资源列表")
    Result findAll();

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询资源数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "资源ID",required=true,paramType="path",dataType="int"),
    })
    Result findById(Integer id);


    /***
     * 新增数据
     * @param resource
     * @return
     */
    @ApiOperation("新增资源")
    Result add(Resource resource);


    /***
     * 修改数据
     * @param resource
     * @param id
     * @return
     */
    @ApiOperation("修改资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "资源ID",required=true, paramType = "path", dataTypeClass = Integer.class),
    })
    Result update(Resource resource, Integer id);


    /***
     * 根据ID删除资源数据
     * @param id
     * @return
     */
    @ApiOperation("删除资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "资源ID",required=true,paramType="path",dataType="int"),
    })
    Result delete(Integer id);

    /***
     * 多条件搜索资源数据
     * @param searchMap
     * @return
     */
    @ApiOperation("查询资源列表")
    Result findList(Map searchMap);


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询资源列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="searchMap",value = "条件查询 JSON数据， 资源名称或者是资源首字母"),
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message="Indicates ..."),
//            @ApiResponse(code = 404, message = "not found error")
//    })
    Result findPage(Map searchMap, Integer page, Integer size);

}
