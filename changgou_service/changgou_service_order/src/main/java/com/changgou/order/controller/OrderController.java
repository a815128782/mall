package com.changgou.order.controller;

import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.order.OrderCode;
import com.changgou.order.api.OrderApi;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.pojo.OrderList;
import com.changgou.order.service.OrderItemService;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.pojo.Vo;
import com.changgou.order.service.OrderItemService;
import com.changgou.order.service.OrderService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController implements OrderApi {


    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    TokenDecode tokenDecode;


    /*
        修改订单支付状态
    * */

    @GetMapping("/updatepay/{id}")
    public Result update(@PathVariable("id") String id){
        orderService.updateById(id);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    /*
    * 根据id删除订单
    * */
    @GetMapping("/deletepay/{id}")
    public Result deletePay(@PathVariable("id")String id){
        orderService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }
    /*
    * 修改发货状态
    * */
    @GetMapping("/updatesend/{id}")
    public Result updateSend(@PathVariable("id") String id){
        orderService.updateConginById(id);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /*
     * 根据用户名查询订单
     * */
    @GetMapping("/findOrderByUserName")
    public Result findOrderByUserName() {
        //获取当前登录人名称
        String username = tokenDecode.getUserInfo().get("username");
        List<Order> orderList = orderService.findOrderByUserName(username);

        List<Vo> voList = new ArrayList<>();

        for (Order order : orderList) {
            Vo vo = new Vo();
            String payStatus = order.getPayStatus();
            vo.setPay_Status(payStatus);
            String orderId = order.getId();
            List<OrderItem> orderItemList = orderItemService.findOrderItemByOrderId(orderId);
            vo.setOrderItemList(orderItemList);
            voList.add(vo);
        }

        return new Result(true, StatusCode.OK, "订单查询成功",voList);
    }

    //根据用户查询订单
    @GetMapping("/pay")
    public Result myOrder(){
        String username = tokenDecode.getUserInfo().get("username");
//        String username="heima";
        List list = new ArrayList();
        List<Order> order = orderService.findOrderByUsername(username);
        for (Order orders : order) {
            OrderList orderList =new OrderList();
            String id = orders.getId();
            List<OrderItem> orderItemList = orderItemService.findByOrderId(id);
            String payStatus = orders.getPayStatus();
            orderList.setPay_status(payStatus);
            orderList.setOrderItem(orderItemList);
            String consignStatus = orders.getConsignStatus();
            orderList.setConsign_status(consignStatus);
//            orderList.setOrder(order);
            list.add(orderList);
        }

        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Order> orderList = orderService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", orderList);
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Order> findById(@PathVariable String id) {
        Order order = orderService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", order);
    }


    /***
     * 新增数据
     * @param order
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Order order) {
        //获取登录人名称
        String username = tokenDecode.getUserInfo().get("username");
        order.setUsername(username);
        String orderId = null;
        try {
            orderId = orderService.add(order);
        } catch (RuntimeException e) {
            ExceptionCast.cast(OrderCode.ADD_ORDER_ERROR);
        }
        return R.T("添加", orderId);
    }


    /***
     * 修改数据
     * @param order
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Order order, @PathVariable String id) {
        order.setId(id);
        orderService.update(order);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param
     * @return
     */
//    @DeleteMapping(value = "/{id}")
    @RequestMapping("/delete")
    public Result delete(@RequestParam("orderId")String orderId) {
        orderService.delete(orderId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search")
    public Result findList(@RequestBody Map searchMap) {
        List<Order> list = orderService.findList(searchMap);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result findPage(@RequestBody Map searchMap, @PathVariable Integer page, @PathVariable Integer size) {
        Page<Order> pageList = orderService.findPage(searchMap, page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    @PostMapping("/batchSend")
    public Result batchSend(@RequestBody List<Order> orders) {
        orderService.batchSend(orders);
        return R.T("发货成功");
    }

    /**
     * 完成评价后修改订单评价状态
     * @param orderId
     * @return
     */
    @PutMapping("/comment/{id}")
    public Result updateOrderCommentStatus(@PathVariable("id") String orderId){
        orderService.updateOrderCommentStatus(orderId);
        return new Result(true,StatusCode.OK,"订单完成评价");
    }

    @PutMapping("/{id}/{transaction_id}")
    void updateOrderStatus(@PathVariable("id") String out_trade_no,@PathVariable("transaction_id") String trade_no){
        orderService.updatePayStatus(out_trade_no,trade_no);
    }

}
