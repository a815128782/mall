package com.changgou.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiXiang
 */
@Configuration
public class RabbitMQConfig {

    public static final String ORDER_TACK="order_tack";
    public static final String COMMENT_TACK="comment_tack";

    @Bean
    public Queue queue(){
        return new Queue(ORDER_TACK);
    }

    @Bean(name = COMMENT_TACK)
    public Queue COMMENT_TACK(){
        return new Queue(COMMENT_TACK);
    }
}
