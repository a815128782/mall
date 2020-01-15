package com.changgou.comment.listener;

import com.changgou.comment.config.RabbitMQConfig;
import com.changgou.comment.service.CommentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author:Administrator
 * @Date: 2020/1/15 14:52
 */
@Component
public class CommentTaskListener {

    @Autowired
    private CommentService commentService;

    @RabbitListener(queues = RabbitMQConfig.COMMENT_TACK)
    public void receiveOrderTaskMessage(String message) {
        System.out.println("收到自动更新评论数的消息" + message);

        //调用业务层,完成自动收货实现
        commentService.AutoCount();
    }
}
