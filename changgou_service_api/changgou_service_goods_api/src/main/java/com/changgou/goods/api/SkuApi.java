package com.changgou.goods.api;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Sku;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @author LiXiang
 */
@Api(value = "Sku管理接口", description = "Sku管理接口，提供页面的增、删、改、查")
public interface SkuApi {

    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有Sku列表")
    Result findAll();

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询Sku数据")
    @ApiImplicitParam(name = "id",value = "Skuid",required = true,paramType = "path",dataTypeClass = String.class)
    Result findById(String id);


    /***
     * 新增数据
     * @param sku
     * @return
     */
    @ApiOperation("新增Sku")
    Result add(Sku sku);


    /***
     * 修改数据
     * @param sku
     * @param id
     * @return
     */
    @ApiOperation("修改Sku数据")
    @ApiImplicitParam(name="id",value = "SkuID",required = true,paramType = "path",dataTypeClass = String.class)
    Result update(Sku sku, String id);


    /***
     * 根据ID删除Sku数据
     * @param id
     * @return
     */
    @ApiOperation("删除Sku数据")
    @ApiImplicitParam(name="id",value = "SkuID",required = true,paramType = "path",dataTypeClass = String.class)
    Result delete(String id);


    /***
     * 多条件搜索Sku数据
     * @param searchMap
     * @return
     */
    @ApiOperation(value = "条件查询Sku列表",notes = "条件为空时传递参数为 {}")
    Result findList(Map searchMap);


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询Sku列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMap", value = "条件查询 JSON数据， exc,xid,skuStatus;无条件时 传递 {}"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示数据", required = true, paramType = "path", dataType = "int")
    })
    Result findPage(Map searchMap, int page, int size);
}
