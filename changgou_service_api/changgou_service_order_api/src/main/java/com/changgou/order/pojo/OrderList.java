package com.changgou.order.pojo;

import java.io.Serializable;
import java.util.List;

public class OrderList implements Serializable {

    private List<Order> Order;
    private List<OrderItem> OrderItem;
    private String Pay_status;
    private String Consign_status;

    public String getConsign_status() {
        return Consign_status;
    }

    public void setConsign_status(String consign_status) {
        Consign_status = consign_status;
    }

    public String getPay_status() {
        return Pay_status;
    }

    public void setPay_status(String pay_status) {
        Pay_status = pay_status;
    }

    public List<com.changgou.order.pojo.Order> getOrder() {
        return Order;

    }

    public void setOrder(List<com.changgou.order.pojo.Order> order) {
        Order = order;
    }

    public List<com.changgou.order.pojo.OrderItem> getOrderItem() {
        return OrderItem;
    }

    public void setOrderItem(List<com.changgou.order.pojo.OrderItem> orderItem) {
        OrderItem = orderItem;
    }
}
