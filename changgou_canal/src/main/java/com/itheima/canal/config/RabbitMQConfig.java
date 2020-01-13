package com.itheima.canal.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiXiang
 */
@Configuration
public class RabbitMQConfig {
    //定义商品上架交换机名称
    public static final String GOODS_UPDATE_EXCHANGE = "goods_update_exchange";
    //定义商品下架交换机名称
    public static final String GOODS_DOWN_EXCHANGE = "goods_down_exchange";

    public static final String PAGE_DELETE_EXCHANGE = "page_delete_exchange";

    //定义商品消息队列名称
    public static final String SEARCH_ADD_QUEUE = "search_add_queue";
    public static final String SEARCH_DEL_QUEUE = "search_del_queue";
    //静态页面生成队列
    public static final String PAGE_CREATE_QUEUE = "page_create_queue";
    //静态页面删除页面
    public static final String PAGE_DELETE_QUEUE = "page_delete_queue";

    //定义广告消息队列名称
    public static final String AD_UPDATE_QUEUE = "ad_update_queue";

    //声明队列
    @Bean
    public Queue queue() {
        return new Queue(AD_UPDATE_QUEUE);
    }

    @Bean(SEARCH_ADD_QUEUE)
    public Queue SEARCH_ADD_QUEUE() {
        return new Queue(SEARCH_ADD_QUEUE);
    }

    @Bean(SEARCH_DEL_QUEUE)
    public Queue SEARCH_DEL_QUEUE() {
        return new Queue(SEARCH_DEL_QUEUE);
    }

    @Bean(PAGE_CREATE_QUEUE)
    public Queue PAGE_CREATE_QUEUE() {
        return new Queue(PAGE_CREATE_QUEUE);
    }

    @Bean(PAGE_DELETE_QUEUE)
    public Queue PAGE_DELETE_QUEUE() {
        return new Queue(PAGE_DELETE_QUEUE);
    }




    //声明上架交换机
    @Bean(GOODS_UPDATE_EXCHANGE)
    public Exchange GOODS_UPDATE_EXCHANGE() {
        return ExchangeBuilder.fanoutExchange(GOODS_UPDATE_EXCHANGE).durable(true).build();
    }

    //商品下架交换机
    @Bean(GOODS_DOWN_EXCHANGE)
    public Exchange GOODS_DOWN_EXCHANGE() {
        return ExchangeBuilder.fanoutExchange(GOODS_DOWN_EXCHANGE).durable(true).build();
    }

    @Bean(PAGE_DELETE_EXCHANGE)
    public Exchange PAGE_DELETE_EXCHANGE() {
        return ExchangeBuilder.fanoutExchange(PAGE_DELETE_EXCHANGE).durable(true).build();
    }



    //队列与交换机的绑定
    @Bean
    public Binding GOODS_UPDATE_EXCHANGE_BINDING(@Qualifier(SEARCH_ADD_QUEUE)Queue queue,@Qualifier(GOODS_UPDATE_EXCHANGE)Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }

    @Bean
    public Binding PAGE_DELETE_QUEUE_BINDING(@Qualifier(PAGE_DELETE_QUEUE)Queue queue,@Qualifier(PAGE_DELETE_EXCHANGE)Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }

    @Bean
    public Binding PAGE_CREATE_QUEUE_BINDING(@Qualifier(PAGE_CREATE_QUEUE)Queue queue,@Qualifier(GOODS_UPDATE_EXCHANGE)Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }

    @Bean
    public Binding GOODS_DOWN_EXCHANGE_BINDING(@Qualifier(SEARCH_DEL_QUEUE)Queue queue,@Qualifier(GOODS_DOWN_EXCHANGE)Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }
}
