package com.itheima.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.itheima.canal.config.RabbitMQConfig;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZJ
 */
@CanalEventListener //声明当前的类是canal的监听类
public class BusinessListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     *
     * @param eventType 当前操作数据库的类型
     * @param rowData 当前操作数据库的数据
     * schema 声明当前操作的数据库名称
     * table  声明当前操作的表名称
     */
    @ListenPoint(schema = "changgou_business", table = {"tb_ad"})
    public void adUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("广告数据发生变化");

//        rowData.getBeforeColumnsList().forEach(((column -> System.out.println("改变前的数据:"+column.getName()+"::"+column.getValue()))));

        //修改前数据
        /*for(CanalEntry.Column column: rowData.getBeforeColumnsList()) {
            if(column.getName().equals("position")){
                System.out.println("发送消息到mq  ad_update_queue:"+column.getValue());
                rabbitTemplate.convertAndSend("","ad_update_queue",column.getValue());  //发送消息到mq
                break;
            }
        }*/

//        rowData.getAfterColumnsList().forEach(((column -> System.out.println("改变后的数据:"+column.getName()+"::"+column.getValue()))));




        //修改后数据
        /*for(CanalEntry.Column column: rowData.getAfterColumnsList()) {
            if("position".equals(column.getName())){
                System.out.println("发送最新的数据到mq  ad_update_queue:"+column.getValue());
                rabbitTemplate.convertAndSend("", RabbitMQConfig.AD_UPDATE_QUEUE,column.getValue());  //发送消息到mq
            }
        }*/

        /**
         * 解决修改的是广告的position问题

         * 实现思路：
         * 1. 在Canal Server端监听得到原有oldposition和newposition数据
         * 2. 比较两个值是否相等
         * 3. 如果不相等我们需要更新原有的 oldposition和newposition的数据列表
         * 4. 如果相等则只更新newposition数据列表即可
         */
        //获取改变之前的数据并转换为Map
        Map<String,String> oldData = new HashMap<>();
        rowData.getBeforeColumnsList().forEach((c -> oldData.put(c.getName(),c.getValue())));

        //获取改变之后的数据并转换为Map
        Map<String,String> newData = new HashMap<>();
        rowData.getAfterColumnsList().forEach((c)->newData.put(c.getName(),c.getValue()));

        //// 位置修改发送position更新数据即可
        if(!oldData.get("position").equals(newData.get("position"))) {
            rabbitTemplate.convertAndSend("", RabbitMQConfig.AD_UPDATE_QUEUE,oldData.get("position"));
        }
        rabbitTemplate.convertAndSend("",RabbitMQConfig.AD_UPDATE_QUEUE, newData.get("position"));
    }
}
