package com.changgou.page.listener;

import com.changgou.page.config.RabbitMQConfig;
import com.changgou.page.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiXiang
 */
@Component
public class PageListener {

    @Autowired
    PageService pageService;

    @RabbitListener(queues = RabbitMQConfig.PAGE_CREATE_QUEUE)
    public void receiveMessage(String spuId){
        System.out.println("获取静态化页面的商品id的值为: " +spuId);
        //调用业务层完成静态化页面生成
        try {
            pageService.generateHTML(spuId);
            System.out.println("静态化页面生成完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.PAGE_DELETE_QUEUE)
    public void receiveDelMessage(String spuId){
        System.out.println("获取静态化页面的商品id的值为: " +spuId);
        //调用业务层完成静态化页面生成
        try {
            pageService.delHTML(spuId);
            System.out.println("静态化页面删除完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
