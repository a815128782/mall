package com.changgou.user.service;

import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.user.pojo.Collect;
import com.changgou.user.pojo.Footmark;

import java.util.List;

/**
 * @Author:sss
 * @Date:2020/1/13 15:42
 */

public interface CollectService {


    //添加收藏
    void add(String username, String skuId);

    //添加足迹
    void addFootMark(String username, String skuId);

    List<Sku> list(String username);

    List<Sku> list2FootMark(String username);

    List<Collect> findBySkuId(String id);

    List<Footmark> findSkuById(String id);

    void deleteFootMark(String id);

    void deleteCollect(String id);
}
