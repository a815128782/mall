package com.changgou.order.service;

import java.util.Map;

/**
 * @author LiXiang
 */
public interface CartService {

    /**
     * 添加购物车
     * @param skuId 商品的ID
     * @param num 商品的数量
     */
    void addCart(String skuId,Integer num,String username,Integer type);

    /**
     * 查询购物车
     * @param username
     * @return
     */
    Map list(String username);
}
