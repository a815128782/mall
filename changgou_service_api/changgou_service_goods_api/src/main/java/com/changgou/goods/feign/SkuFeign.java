package com.changgou.goods.feign;


import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LiXiang
 */
@FeignClient(name="goods")
public interface SkuFeign {

    /**
     * 一次性全部导入
     * @param spuId
     * @return
     */
    @GetMapping("/sku/spu/{spuId}")
    List<Sku> findSkuListBySpuId(@PathVariable("spuId") String spuId);

    /**
     * 分页导入
     * @param spuId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/sku/spu/page/{spuId}")
    PageResult<Sku> findPageSkuListBySpuId(@PathVariable("spuId") String spuId, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);

    /**
     * 订单微服务
     * 根据ID查询sku商品信息
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable("id") String id);


    /**
     * 扣减库存
     * @param username
     * @return
     */
    @PostMapping("/sku/decr/count")
    public Result decrCount(@RequestParam("username") String username);


    /**
     * 库存回滚
     * @param skuId
     * @param num
     * @return
     */
    @RequestMapping("/sku/resumeStockNum")
    public Result resumeStockNum(@RequestParam("skuId") String skuId,@RequestParam("num") Integer num);
}
