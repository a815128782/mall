package com.changgou.pay.service;

import java.util.Map;

/**
 * @author LiXiang
 */
public interface WXPayService {

    Map nativePay(String orderId,String payMoney);

    /**
     * 基于微信查询订单
     */
    Map queryOrder(String orderId);

    /**
     * 基于微信关闭订单
     * @param orderId
     * @return
     */
    Map closeOrder(String orderId);
}
