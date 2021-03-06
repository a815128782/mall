package com.changgou.order.service;

import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderList;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface OrderService {



    /*
    *  根据用户名查询已发货订单
    * */
    List<Order> findConsignByUsername(String username);



    /*
    *  根据用户名查询订单
    * */
    List<Order> findOrderByUserName(String username);

    /***
     * 查询所有
     * @return
     */
    List<Order> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Order findById(String id);

    /***
     * 新增
     * @param order
     */
    String add(Order order) throws RuntimeException;

    /***
     * 修改
     * @param order
     */
    void update(Order order);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Order> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Order> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Order> findPage(Map<String, Object> searchMap, int page, int size);


    /**
     * 修改订单的支付状态,并记录日志
     * @param orderId
     * @param transaction_id
     */
    void updatePayStatus(String orderId, String transaction_id);

    /**
     * 关闭订单
     * @param message
     */
    void closeOrder(String message);

    /**
     * 订单批量发送
     * @param orders
     */
    void batchSend(List<Order> orders);

    /**
     * 手动收货
     * @param orderId
     * @param operator
     */
    void confirmTask(String orderId,String operator);

    /**
     * 自动收货
     */
    void autoTack();


    List<Order> findOrderByUsername(String username);

    /**
     * 完成评价后修改订单评价状态
     * @param orderId
     */
    void updateOrderCommentStatus(String orderId);

    /*
    * 修改发货状态
    * */
    void updateConsignStatus(String orderId);

    void updateById(String id);

    void updateConginById(String id);
}
