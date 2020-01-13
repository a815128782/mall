package com.changgou.goods.api;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Goods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @author LiXiang
 */
@Api(value = "商品管理接口", description = "商品管理接口，提供页面的增、删、改、查")
public interface SpuApi {

    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有商品列表")
    Result findAll();

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询商品数据")
    @ApiImplicitParam(name = "id",value = "Spuid",required = true,paramType = "path",dataTypeClass = String.class)
    Result findById(String id);


    /***
     * 新增数据
     * @param goods
     * @return
     */
    @ApiOperation("新增商品")
    Result add(Goods goods);


    /***
     * 修改数据
     * @param goods
     * @param id
     * @return
     */
    @ApiOperation("修改商品数据")
    @ApiImplicitParam(name="id",value = "SpuID",required = true,paramType = "path",dataTypeClass = String.class)
    Result update(Goods goods, String id);


    /***
     * 根据ID删除Spu数据
     * @param id
     * @return
     */
    @ApiOperation("删除商品数据")
    @ApiImplicitParam(name="id",value = "SpuID",required = true,paramType = "path",dataTypeClass = String.class)
    Result delete(String id);


    /***
     * 多条件搜索Spu数据
     * @param searchMap
     * @return
     */
    @ApiOperation(value = "条件查询商品列表",notes = "条件为空时传递参数为 {}")
    Result findList(Map searchMap);


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMap", value = "条件查询 JSON数据， exc,xid,spuStatus;无条件时 传递 {}"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示数据", required = true, paramType = "path", dataType = "int")
    })
    Result findPage(Map searchMap, int page, int size);



    /**
     * 商品审核
     * @param id
     * @return
     */
    @ApiOperation("商品审核")
    @ApiImplicitParam(name="id",value = "商品ID",required = true,paramType = "path",dataTypeClass = String.class)
    Result audit(String id);

    /**
     * 商品下架
     * @param id
     * @return
     */
    @ApiOperation("商品下架")
    @ApiImplicitParam(name="id",value = "商品ID",required = true,paramType = "path",dataTypeClass = String.class)
    Result pull(String id);


    /**
     * 商品上架
     * @param id
     * @return
     */
    @ApiOperation("商品上架")
    @ApiImplicitParam(name="id",value = "商品ID",required = true,paramType = "path",dataTypeClass = String.class)
    Result put(String id);


    /**
     * 商品逻辑删除还原
     * @param id
     * @return
     */
    @ApiOperation("商品逻辑删除还原")
    @ApiImplicitParam(name="id",value = "商品ID",required = true,paramType = "path",dataTypeClass = String.class)
    Result restore(String id);

    /**
     * 商品的物理删除
     * @param id
     * @return
     */

    @ApiOperation("商品的物理删除")
    @ApiImplicitParam(name="id",value = "商品ID",required = true,paramType = "path",dataTypeClass = String.class)
    Result readDel(String id);
}
