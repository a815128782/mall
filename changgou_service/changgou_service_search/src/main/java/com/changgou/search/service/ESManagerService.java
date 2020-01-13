package com.changgou.search.service;

/**
 * @author LiXiang
 */
public interface ESManagerService {
    //创建索引库结构
    void createMappingAndIndex();


    //导入全部数据进入es
    void importAll();

    void importPageAll();


    //根据 spuId 查询skuList,再导入索引库
    void importDataBySpuId(String spuId);

    //根据spuId删除 es 索引库中的相关数据
    void delDateBySpuId(String spuId);



}
