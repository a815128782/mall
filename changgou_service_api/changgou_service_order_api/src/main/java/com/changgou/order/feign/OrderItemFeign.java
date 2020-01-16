package com.changgou.order.feign;

import com.changgou.common.entity.Result;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author LiXiang
 */
@FeignClient(name = "order")
public interface OrderItemFeign {

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/orderItem/search" )
    public Result<List<OrderItem>> findList(@RequestBody Map searchMap);

    @GetMapping("/orderItem/{id}")
    public Result findById(@PathVariable String id);
}
