package com.changgou.order.dao;

import com.changgou.order.pojo.Order;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;


public interface OrderMapper extends Mapper<Order> {
//    @Select("select * from tb_order where username=#{username} and consign_status=1")
//    List<Order> findConsignByUsername(@Param("username") String username);
    @Select("UPDATE tb_order SET consign_status='1' WHERE id = #{id}")
    void updateCsById(@Param("id") String id);
    @Select("UPDATE tb_order SET pay_status='1' WHERE id = #{id}")
    void updatepyById(String id);

//    @Select("select * from tb_order where username = #{username}")
//    List<Order> findOrderByUsername(@Param("username") String username);
}
