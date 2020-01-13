package com.changgou.seckill.service.impl;

import com.changgou.common.util.IdWorker;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author LiXiang
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    public static final String SECKILL_GOODS_KEY="seckill_goods_";

    public static final String SECKILL_GOODS_STOKE_COUNT_KEY="seckill_goods_stoke_count_";

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    IdWorker idWorker;
    @Override
    public boolean add(Long id, String time, String username) {
        /**
         * 1.获取redis中的商品信息与库存信息，并进行判断
         * 2.执行redis的与扣减库存操作，并获取扣减之后的库存值
         * 3.如果扣减之后的库存值 <=0,则删除redis中相应的商品信息与库存信息
         * 4.基于mq完成mysql的数据同步，进行一步下单并扣减库存（mysql）
         */
        //获取商品信息
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SECKILL_GOODS_KEY+time).get(id);
        //获取库存信息
        String redisStock = (String) redisTemplate.opsForValue().get(SECKILL_GOODS_STOKE_COUNT_KEY+id);
        if(StringUtils.isEmpty(redisStock)) {
            return false;
        }
        int stock = Integer.parseInt(redisStock);
        if(seckillGoods == null || stock <= 0) {
            return false;
        }

        //执行redis的预扣减库存，并获取到扣减之后的库存值
        //decrement:减 （原子性操作）
        //increment:加
        long decrement = redisTemplate.opsForValue().decrement(SECKILL_GOODS_STOKE_COUNT_KEY + id);
        if(decrement <= 0) {
            //扣减完库存之后，当前商品已经没有库存了
            //删除redis中的商品信息与库存信息
            redisTemplate.boundHashOps(SECKILL_GOODS_KEY+time).delete(id);
            redisTemplate.delete(SECKILL_GOODS_STOKE_COUNT_KEY+id);
        }

        //发送消息（保证消息生产者对于消息的不丢失实现）
        //消息体：秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setSeckillId(id);
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setUserId(username);
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");

        //发送消息
        //TODO
        return true;
    }
}
