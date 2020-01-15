package com.changgou.order.pojo;


import java.io.Serializable;
import java.util.List;

public class Vo implements Serializable{
    private  List<OrderItem> orderItemList;
    private  String  pay_Status;

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String getPay_Status() {
        return pay_Status;
    }

    public void setPay_Status(String pay_Status) {
        this.pay_Status = pay_Status;
    }


}
