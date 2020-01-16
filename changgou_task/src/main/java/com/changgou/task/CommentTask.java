package com.changgou.task;

import com.changgou.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author:Administrator
 * @Date: 2020/1/15 14:38
 */
@Component
public class CommentTask {
    @Autowired
    RabbitTemplate rabbitTemplate;

//     */5 * * * * ?
    // "0 0 2 * * ?"  // 每天两点
    @Scheduled(cron = "*/5 * * * * ?")
    public void autoUpdateSkuComment() {
        rabbitTemplate.convertAndSend("", RabbitMQConfig.COMMENT_TACK,"-");
    }
}
