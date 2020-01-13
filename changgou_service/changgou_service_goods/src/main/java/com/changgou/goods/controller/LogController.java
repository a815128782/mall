package com.changgou.goods.controller;

import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.goods.api.LogApi;
import com.changgou.goods.pojo.Log;
import com.changgou.goods.service.LogService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/log")
public class LogController implements LogApi {


    @Autowired
    private LogService logService;

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Log> logList = logService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",logList) ;
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        Log log = logService.findById(id);
        return new Result(true, StatusCode.OK,"查询成功",log);
    }


    /***
     * 新增数据
     * @param log
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Log log){
        logService.add(log);
        return new Result(true, StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param log
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Log log, @PathVariable Long id){
        log.setId(id);
        logService.update(log);
        return new Result(true, StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        logService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search" )
    public Result findList(@RequestBody Map searchMap){
        List<Log> list = logService.findList(searchMap);
        return new Result(true, StatusCode.OK,"查询成功",list);
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
        Page<Log> pageList = logService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }


}
