package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SpuService {

    /***
     * 查询所有
     * @return
     */
    List<Spu> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Spu findById(String id);

    /***
     * 新增
     * @param goods 封装 spu sku的实体类
     */
    void add(Goods goods);

    /***
     * 修改
     * @param goods
     */
    void update(Goods goods);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Spu> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(Map<String, Object> searchMap, int page, int size);


    /**
     * 根据ID查询spu和sku列表
     * @param id
     * @return
     */
    Goods findGoodsById(String id);

    /**
     * 审核
     * @param id spuId
     */
    void audit(String id);

    /**
     * 商品下架
     */
    void pull(String id);

    /**
     * 商品上架
     */
    void put(String id);

    /**
     * 还原商品
     */
    void restore(String id);

    /**
     * 物理删除,真正的删除
     * @param id 商品 id
     */
    void readDel(String id);
}
