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
 * 商品上架监听类
 * @author ZJ
 */
@CanalEventListener //声明当前类是Canal监听类
public class SpuListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * spu 表更新
     * @param eventType
     * @param rowData
     */
    @ListenPoint(schema = "changgou_goods", table = {"tb_spu"},eventType = CanalEntry.EventType.UPDATE )
    public void spuUp(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("tb_spu表数据发生变化");

        //修改前数据
        Map<String,String> oldDate=new HashMap<>();
        /*for(CanalEntry.Column column: rowData.getBeforeColumnsList()) {
            oldDate.put(column.getName(),column.getValue());
        }*/
        rowData.getBeforeColumnsList().forEach((c)->oldDate.put(c.getName(),c.getValue()));
        rowData.getBeforeColumnsList().forEach((c)-> System.out.println("更新前："+c.getName()+"~~~~~~"+c.getValue()));
        //修改后数据
        Map<String,String>  newData=new HashMap<>();
        /*for(CanalEntry.Column column: rowData.getAfterColumnsList()) {
            newData.put(column.getName(),column.getValue());
        }*/
        rowData.getAfterColumnsList().forEach((c)->newData.put(c.getName(),c.getValue()));
        rowData.getAfterColumnsList().forEach((c)-> System.out.println("更新后："+c.getName()+"~~~~~~"+c.getValue()));

        //is_marketable  由0改为1表示上架
        if("0".equals(oldDate.get("is_marketable")) && "1".equals(newData.get("is_marketable")) ){
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_UPDATE_EXCHANGE,"",newData.get("id")); //发送到mq商品上架交换器上
        }


        //获取最细你下架的商品
        if("1".equals(oldDate.get("is_marketable")) && "0".equals(newData.get("is_marketable")) ){
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_DOWN_EXCHANGE,"",newData.get("id")); //发送到mq商品上架交换器上
        }

        //获取最新被审核通过的商品 status 0-->1
        if("0".equals(oldDate.get("status"))&&"1".equals(newData.get("status"))) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.GOODS_UPDATE_EXCHANGE,"",newData.get("id"));
        }

        //获取最新被审核通过的商品 status 1-->0
        if("1".equals(oldDate.get("status"))&&"0".equals(newData.get("status"))) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.PAGE_DELETE_EXCHANGE,"",newData.get("id"));
        }

    }
}
