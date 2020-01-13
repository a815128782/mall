package com.changgou.goods.controller;

import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.goods.api.SkuApi;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/sku")
public class SkuController implements SkuApi {


    @Autowired
    private SkuService skuService;

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Sku> skuList = skuService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",skuList) ;
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable("id") String id){
        Sku sku = skuService.findById(id);
        return new Result(true, StatusCode.OK,"查询成功",sku);
    }


    /***
     * 新增数据
     * @param sku
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Sku sku){
        skuService.add(sku);
        return new Result(true, StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param sku
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Sku sku, @PathVariable String id){
        sku.setId(id);
        skuService.update(sku);
        return new Result(true, StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        skuService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search" )
    public Result findList(@RequestBody Map searchMap){
        List<Sku> list = skuService.findList(searchMap);
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
        Page<Sku> pageList = skuService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }

    @GetMapping("/spu/{spuId}")
    public List<Sku> findSkuListBySpuId(@PathVariable("spuId")String spuId){
        Map<String,Object> searchMap = new HashMap<>();
        /**
         * all 是查询所有status为 1 的
         */
        if(!"all".equals(spuId)){
            searchMap.put("spuId",spuId);
        }
        searchMap.put("status","1");
        List<Sku> skuList = skuService.findList(searchMap);
        return skuList;
    }


    @GetMapping("/spu/page/{spuId}")
    public PageResult<Sku> findPageSkuListBySpuId(@PathVariable("spuId") String spuId,  Integer pageNum,Integer pageSize){
        Map<String,Object> searchMap = new HashMap<>();

        if(!"all".equals(spuId)){
            searchMap.put("spuId",spuId);
        }
        searchMap.put("status","1");
        Page<Sku> pageSkuList = skuService.findPage(searchMap,pageNum,pageSize);

        return new PageResult<>(pageSkuList.getTotal(),pageSkuList);
    }

    @PostMapping("/decr/count")
    public Result decrCount(@RequestParam("username") String username){
        skuService.decrCount(username);
        return R.T("库存扣减");
    }

    /**
     * 库存回滚
     * @param skuId
     * @param num
     * @return
     */
    @RequestMapping("/resumeStockNum")
    public Result resumeStockNum(@RequestParam("skuId") String skuId,@RequestParam("num") Integer num) {
        skuService.resumeStockNum(skuId,num);
        return R.T("回滚库存成功");
    }

}
