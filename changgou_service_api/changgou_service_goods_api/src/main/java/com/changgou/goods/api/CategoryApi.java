package com.changgou.goods.api;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @Description:
 * @Version: V1.0
 */
@Api(value="种类管理接口",description = "种类管理接口，提供页面的增、删、改、查")
public interface CategoryApi {


    /***
     * 查询所有
     * @return
     */
    @ApiOperation("查询所有种类列表")
    Result findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询种类数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "种类ID",required=true,paramType="path",dataType="int"),
    })
    Result findById(Integer id);

    /***
     * 新增
     * @param category
     */
    @ApiOperation("新增种类")
    Result add(Category category);

    /***
     * 修改
     * @param category
     */
    @ApiOperation("修改种类")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id",value = "种类ID",required = true,paramType = "path", dataTypeClass = Integer.class)
    )
    Result update(Category category, Integer id);

    /***
     * 删除
     * @param id
     */
    @ApiOperation("删除种类")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id",value = "种类ID",required = true,paramType = "path", dataTypeClass = Integer.class)
    )
    Result delete(Integer id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    @ApiOperation("查询种类列表")
    Result findList(Map searchMap);


    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询种类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchMap", value = "条件查询 JSON数据， 种类名称"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    Result findPage(Map searchMap, int page, int size);

    /**
     * 通过Pid查询子分类
     * @param
     * @return
     */
    @ApiOperation("根据父ID查询子分类")
    @ApiImplicitParam(name="id" ,value="父ID,跟分类,parentId为0",required = true,paramType = "path",dataTypeClass = Integer.class)
    Result findListByPid(Integer id);
}
