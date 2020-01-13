package com.changgou.order.api;

import com.changgou.common.entity.Result;
import com.changgou.order.pojo.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @Description:
 * @Version: V1.0
 */
@Api(value="订单管理接口",description = "订单管理接口，提供页面的增、删、改、查")
public interface OrderApi {


    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有订单列表")
    Result findAll();

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询订单数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "订单ID",required=true,paramType="path",dataType="int"),
    })
    Result findById(String id);


    /***
     * 新增数据
     * @param order
     * @return
     */
    @ApiOperation("新增订单")
    Result add(Order order);


    /***
     * 修改数据
     * @param order
     * @param id
     * @return
     */
    @ApiOperation("修改订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "订单ID",required=true, paramType = "path", dataTypeClass = Integer.class),
    })
    Result update(Order order, String id);


    /***
     * 根据ID删除订单数据
     * @param id
     * @return
     */
    @ApiOperation("删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "订单ID",required=true,paramType="path",dataType="int"),
    })
    Result delete(String id);

    /***
     * 多条件搜索订单数据
     * @param searchMap
     * @return
     */
    @ApiOperation("查询订单列表")
    Result findList(Map searchMap);


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="searchMap",value = "条件查询 JSON数据， 订单名称或者是订单首字母"),
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message="Indicates ..."),
//            @ApiResponse(code = 404, message = "not found error")
//    })
    Result findPage(Map searchMap, Integer page, Integer size);

}
