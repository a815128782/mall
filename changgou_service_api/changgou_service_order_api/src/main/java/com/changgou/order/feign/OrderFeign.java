package com.changgou.order.feign;

import com.changgou.common.entity.Result;
import com.changgou.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiXiang
 */
@FeignClient(name = "order")
public interface OrderFeign {

    @PostMapping("/order")
    public Result add(@RequestBody Order order);

    @GetMapping("/order/{id}")
    public Result<Order> findById(@PathVariable("id") String id);

    @PutMapping("/order/{id}/{transaction_id}")
    void updateOrderStatus(@PathVariable("id") String out_trade_no,@PathVariable("transaction_id") String trade_no);


    /**
     * 完成评价后修改订单评价状态
     * @param orderId
     * @return
     */
    @PutMapping("/order/comment/{id}")
    public Result updateOrderCommentStatus(@PathVariable("id") String orderId);
}
