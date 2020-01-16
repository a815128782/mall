package com.changgou.order.dao;

import com.changgou.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface OrderItemMapper extends Mapper<OrderItem> {
//    @Select("SELECT * FROM tb_order_item WHERE order_id = #{id}")
//    List<OrderItem> findByOrderId(@Param("id") String id);
}
