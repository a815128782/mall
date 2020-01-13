package com.changgou.goods.dao;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BrandMapper extends Mapper<Brand> {

    @Select("SELECT name,image from tb_brand where id in (SELECT brand_id FROM tb_category_brand where category_id in (\n" +
            "SELECT id from tb_category WHERE name = '手机')) ORDER BY seq")
    List<Map> findListByCategoryName(@Param("name")String categoryName);
}
