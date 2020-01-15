package com.changgou.order.feign;

import com.changgou.common.entity.Result;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.Vo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author LiXiang
 */
@FeignClient(name = "order")
public interface OrderFeign {

    @PostMapping("/order")
    public Result add(@RequestBody Order order);

    @GetMapping("/order/{id}")
    public Result<Order> findById(@PathVariable String id);

    @GetMapping("/order/findOrderByUserName")
    public Result<List<Vo>> findOrderByUserName();


}
