package com.changgou.seckill.service;

/**
 * @author LiXiang
 */
public interface SeckillOrderService {

    /**
     * 秒杀下单
     * @param id
     * @param time
     * @param username
     * @return
     */
    boolean add(Long id,String time,String username);
}
