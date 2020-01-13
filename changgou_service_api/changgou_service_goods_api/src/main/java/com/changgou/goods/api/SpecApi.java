package com.changgou.goods.api;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Spec;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @author LiXiang
 */
@Api(value = "规格管理接口", description = "规格管理接口，提供页面的增、删、改、查")
public interface SpecApi {

    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有规格列表")
    Result findAll();

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询规格数据")
    @ApiImplicitParam(name = "id",value = "Specid",required = true,paramType = "path",dataTypeClass = Integer.class)
    Result findById(Integer id);


    /***
     * 新增数据
     * @param spec
     * @return
     */
    @ApiOperation("新增规格")
    Result add(Spec spec);


    /***
     * 修改数据
     * @param spec
     * @param id
     * @return
     */
    @ApiOperation("修改规格数据")
    @ApiImplicitParam(name="id",value = "SpecID",required = true,paramType = "path",dataTypeClass = Integer.class)
    Result update(Spec spec, Integer id);


    /***
     * 根据ID删除规格数据
     * @param id
     * @return
     */
    @ApiOperation("删除规格数据")
    @ApiImplicitParam(name="id",value = "规格ID",required = true,paramType = "path",dataTypeClass = Integer.class)
    Result delete(Integer id);


    /***
     * 多条件搜索Spec数据
     * @param searchMap
     * @return
     */
    @ApiOperation(value = "条件查询规格列表",notes = "条件为空时传递参数为 {}")
    Result findList(Map searchMap);


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询规格列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMap", value = "条件查询 JSON数据， exc,xid,specStatus;无条件时 传递 {}"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示数据", required = true, paramType = "path", dataType = "int")
    })
    Result findPage(Map searchMap, int page, int size);
}
