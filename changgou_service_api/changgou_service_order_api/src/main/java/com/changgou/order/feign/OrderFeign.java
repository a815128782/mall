package com.changgou.order.feign;

import com.changgou.common.entity.Result;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderList;
import com.changgou.order.pojo.Vo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
 * @author LiXiang
 */
@FeignClient(name = "order")
public interface OrderFeign {

    @PostMapping("/order")
    public Result add(@RequestBody Order order);

    @GetMapping("/order/{id}")
    public Result<Order> findById(@PathVariable("id") String id);

    @GetMapping("/order/findOrderByUserName")
    public Result<List<Vo>> findOrderByUserName();

    @PutMapping("/order/{id}/{transaction_id}")
    void updateOrderStatus(@PathVariable("id") String out_trade_no,@PathVariable("transaction_id") String trade_no);


    /**
     * 完成评价后修改订单评价状态
     * @param orderId
     * @return
     */
    @PutMapping("/order/comment/{id}")
    public Result updateOrderCommentStatus(@PathVariable("id") String orderId);

    @GetMapping("/order/pay")
    public Result<List<OrderList>> myOrder();


    /*
     *  根据用户名查询已发货订单
     * */
    @RequestMapping("/order/findConsignByUsername")
    public Result<List<Vo>> findConsignByUsername(@RequestParam("username")String username);
}
