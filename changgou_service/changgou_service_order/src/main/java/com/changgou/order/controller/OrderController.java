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
import com.changgou.order.service.OrderService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController implements OrderApi {


    @Autowired
    private OrderService orderService;
    @Autowired
    TokenDecode tokenDecode;

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
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        orderService.delete(id);
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
