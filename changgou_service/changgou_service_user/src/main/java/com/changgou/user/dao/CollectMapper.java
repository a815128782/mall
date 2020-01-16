package com.changgou.user.dao;

import com.changgou.user.pojo.Collect;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author:sss
 * @Date:2020/1/13 15:48
 */

public interface CollectMapper extends Mapper<Collect> {

    @Select("select * from tb_collect where username = #{username} order by id desc")
    List<Collect> findCollectList(@Param("username") String username);
}
