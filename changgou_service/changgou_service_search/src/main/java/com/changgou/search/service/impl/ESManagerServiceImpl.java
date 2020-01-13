package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.common.entity.PageResult;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.goods.GoodsCode;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.ESManagerMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.ESManagerService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author LiXiang
 */
@Service
public class ESManagerServiceImpl implements ESManagerService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ESManagerMapper esManagerMapper;

    /**
     * 创建索引库结构
     */
    @Override
    public void createMappingAndIndex() {
        elasticsearchTemplate.createIndex(SkuInfo.class);
        //创建映射
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    /**
     * 导入全部sku集合进入索引库
     */
    @Override
    public void importAll() {
       importData("all");
    }

    /**
     * 分页导入全部sku集合进入索引库
     */
    @Override
    public void importPageAll(){
        PageResult<Sku> pageSkuList = skuFeign.findPageSkuListBySpuId("all", 1, 10);
        if(pageSkuList == null){
            ExceptionCast.cast(GoodsCode.GOODS_SKU_FIND_ERROR);
        }
        Long total = pageSkuList.getTotal();
        int pageSize = 1000;

        int totalPage = (int) Math.ceil((total/pageSize))+1;

        for (int i = 1; i < totalPage; i++) {
            PageResult<Sku> pageResult = skuFeign.findPageSkuListBySpuId("all", i, pageSize);
            List<Sku> skuList = pageResult.getRows();
            if(skuList == null ||skuList.size()<=0){
                ExceptionCast.cast(GoodsCode.GOODS_SKU_FIND_ERROR);
            }
            //将skuList转换为json
            String jsonSkuList = JSON.toJSONString(skuList);
            //将json转换为skuInfo
            List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);
            for (SkuInfo skuInfo : skuInfoList) {
                //将规格信息转换为Map
                Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
                skuInfo.setSpecMap(specMap);
            }
            //导入索引库
            esManagerMapper.saveAll(skuInfoList);
        }
    }

    /**
     * 根据spuId删除索引库中对应的数据
     * @param spuId 商品Id
     */
    @Override
    public void delDateBySpuId(String spuId) {
        /*List<Sku> skuList = skuFeign.findSkuListBySpuId(spuId);
        if(skuList == null ||skuList.size()<=0){
            ExceptionCast.cast(GoodsCode.GOODS_SKU_FIND_ERROR);
        }
        for (Sku sku : skuList) {
            esManagerMapper.deleteById(Long.parseLong(sku.getId()));
        }*/

        Iterable<SkuInfo>  skuInfos = esManagerMapper.search(QueryBuilders.termQuery("spuId", spuId));
        if (skuInfos != null) {
            esManagerMapper.deleteAll(skuInfos);
        }


        /**
         * delete  版本升级后,必须要有主键   ID
         */
        //esManagerMapper.delete(skuInfo);



    }

    /**
     * 根据spuId查询skuList,添加到索引库
     * @param spuId 商品Id
     */
    @Override
    public void importDataBySpuId(String spuId) {
        importData(spuId);
    }

    /**
     * 自定义方法
     * @param spuId
     */
    private void importData(String spuId){
        //查询sku集合
        List<Sku> skuList= skuFeign.findSkuListBySpuId(spuId);
        if(skuList == null ||skuList.size()<=0){
            ExceptionCast.cast(GoodsCode.GOODS_SKU_FIND_ERROR);
        }
        //将skuList转换为json
        String jsonSkuList = JSON.toJSONString(skuList);
        //将json转换为skuInfo
        List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfoList) {
            //将规格信息转换为Map
            Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
        }
        //导入索引库
        esManagerMapper.saveAll(skuInfoList);
    }
}
