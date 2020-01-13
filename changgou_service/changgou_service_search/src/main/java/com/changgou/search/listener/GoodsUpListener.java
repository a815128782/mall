package com.changgou.search.listener;

import com.changgou.search.config.RabbitMQConfig;
import com.changgou.search.service.ESManagerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiXiang
 */
@Component
public class GoodsUpListener {

    @Autowired
    private ESManagerService esManagerService;

    @RabbitListener(queues = RabbitMQConfig.SEARCH_ADD_QUEUE)
    public void receiveMessage(String supId) {
        System.out.println("接收到的消息为: " + supId);

        //查询skuList,并导入索引库
        esManagerService.importDataBySpuId(supId);
    }
}
