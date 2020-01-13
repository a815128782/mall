package com.changgou.goods.api;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @author LiXiang
 */
@Api(value = "模板管理接口", description = "模板管理接口，提供页面的增、删、改、查")
public interface TemplateApi {


    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有模板列表")
    Result findAll();

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询模板")
    @ApiImplicitParam(name="id",value = "模板ID",required = true,paramType = "path",dataTypeClass = Long.class)
    Result findById(Integer id);


    /***
     * 新增数据
     * @param template
     * @return
     */

    @ApiOperation("新增模板")
    Result add(Template template);


    /***
     * 修改数据
     * @param template
     * @param id
     * @return
     */

    @ApiOperation("修改指定的模板数据")
    @ApiImplicitParam(name="id",value = "模板ID",required = true,paramType = "path",dataTypeClass = Long.class)
    Result update(Template template, @PathVariable Integer id);


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation("删除指定的模板数据")
    @ApiImplicitParam(name="id",value = "模板ID",required = true,paramType = "path",dataTypeClass = Long.class)
    Result delete(Integer id);

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @ApiOperation("查询模板列表")
    Result findList(Map searchMap);


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询模板列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="searchMap",value = "条件查询 JSON数据， 模板名称或模板封面或图片列表"),
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
    Result findPage(Map searchMap, int page, int size);
}
