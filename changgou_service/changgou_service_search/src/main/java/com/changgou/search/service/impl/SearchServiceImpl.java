package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LiXiang
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Map search(Map<String, String> searchMap) {

        Map<String, Object> resultMap = new HashMap<>();
        /**
         * 第一部分:
         *      分装查询对象:NativeSearchQueryBuilder
         */
        if (searchMap != null) {
            //构建查询条件封装对象
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();


            /**
             * Operator.AND 分词,出现的搜索结果中 必须包含所有 如  手机的  搜索结果必须包含: 手  机  的
             * Operator.OR 搜索结果可能更多
             */
            //按照关键字查询
            if (StringUtils.isNotEmpty(searchMap.get("keyWords"))) {
                boolQuery.must(QueryBuilders.matchQuery("name", searchMap.get("keyWords")).operator(Operator.AND));
            }
            //按照品牌进行过滤查询
            if (StringUtils.isNotEmpty(searchMap.get("brand"))) {
                boolQuery.filter().add(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
            }

            //按照规格进行过滤查询
            for (String key : searchMap.keySet()) {
                if (key.startsWith("spec_")) {
                    String value = searchMap.get(key).replace("%2B", "+");
                    boolQuery.filter().add(QueryBuilders.termQuery(("specMap." + key.substring(5) + ".keyword"), value));
                }
            }

            //按照价格进行区间过滤查询
            if (StringUtils.isNotEmpty(searchMap.get("price"))) {
                String[] prices = searchMap.get("price").split("-");
                if (prices.length == 2) {
                    boolQuery.filter(QueryBuilders.rangeQuery("price").lte(prices[1]));
                }
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(prices[0]));
            }


            nativeSearchQueryBuilder.withQuery(boolQuery);


            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            /**
             * 设置分组查询
             */
            //按照品牌进行分组(聚合)查询
            String skuBrand = "skuBrand";
            /**
             * 设置聚合查询的条件
             * field: 索引库中的对应域
             * 相当于如下的 SQL:
             *        select brand_name as skuBrand from tb_sku where name like '%name%' group by brand_name;
             */
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuBrand).field("brandName"));

            //按照规格进行聚合查询
            String skuSpec = "skuSpec";
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuSpec).field("spec.keyword"));

            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


            //开启分页查询
            String pageNum = searchMap.get("pageNum");//当前页
            String pageSize = searchMap.get("pageSize");//每页显示记录数
            if (StringUtils.isEmpty(pageNum) || Integer.parseInt(pageNum) <= 0) {
                pageNum = "1";
            }

            if (StringUtils.isEmpty(pageSize) || Integer.parseInt(pageSize) < 0) {
                pageSize = "30";
            }
            //设置分页
            /**
             * 参数1 :当前页 从0开始
             * 参数2:每页显示多少条
             */
            nativeSearchQueryBuilder.withPageable(PageRequest.of(Integer.parseInt(pageNum) - 1, Integer.parseInt(pageSize)));

            //按照相关字段进行排序查询
            /**
             * 需要的参数:
             *      参数1:排序域
             *      参数2:当前的排序操作(升序ASC,降序DESC)
             */
            if (StringUtils.isNotEmpty(searchMap.get("sortField")) && StringUtils.isNotEmpty(searchMap.get("sortRule"))) {
                if ("ASC".equals(searchMap.get("sortRule"))) {
                    //升序
                    nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(searchMap.get("sortField")).order(SortOrder.ASC));
                } else {
                    //降序
                    nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(searchMap.get("sortField")).order(SortOrder.DESC));
                }
            }
            //设置高亮域以及高亮的样式
            HighlightBuilder.Field field = new HighlightBuilder.Field("name")//高亮域
                    .preTags("<span style='color:red'>")//高亮样式的前缀
                    .postTags("</span>");//高亮样式的后缀
            nativeSearchQueryBuilder.withHighlightFields(field);


            //################################################################################################################################
            //2.开启查询
            /**
             * 第二部分:执行查询,并返回查询结果 : AggregatedPage
             *          参数1:条件构建对象
             *          参数2:查询操作实体类
             *          参数3:查询结果的操作对象
             */
            AggregatedPage<SkuInfo> resultInfo = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                    //查询结果的相关操作
                    List<T> list = new ArrayList<>();

                    //获取查询命中结果数据
                    SearchHits hits = searchResponse.getHits();
                    if (hits != null) {
                        //有查询结果
                        for (SearchHit hit : hits) {
                            //SearchHit转换为SkuInfo对象
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);

                            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                            if (highlightFields != null && highlightFields.size() > 0) {
                                //替换数据
                                skuInfo.setName(highlightFields.get("name").getFragments()[0].toString());
                            }

                            list.add((T) skuInfo);
                        }
                    }
                    return new AggregatedPageImpl<T>(list, pageable, hits.getTotalHits(), searchResponse.getAggregations());
                }
            });
            //################################################################################################################################



            /**
             * 第三部分: 封装查询结果
             */
            //封装查询结果(最终的返回结果)
            //总记录数
            resultMap.put("total", resultInfo.getTotalElements());
            //总页数
            resultMap.put("totalPage", resultInfo.getTotalPages());
            //数据的集合
            resultMap.put("rows", resultInfo.getContent());


            //封装品牌的分组结果
            StringTerms brandTerms = (StringTerms) resultInfo.getAggregation(skuBrand);
            List<String> brandList = brandTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("brandList", brandList);

            //封装规格的分组结果
            StringTerms specTerms = (StringTerms) resultInfo.getAggregation(skuSpec);
            List<String> specList = specTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("specList", this.formatSpec(specList));

            //当前页
            resultMap.put("pageNum", pageNum);
            return resultMap;
        }
        return null;
    }

    /**
     *  [
     *         "{'颜色': '黑色'}",
     *         "{}",
     *         "{'颜色': '红色'}",
     *         "{'颜色': '黑色', '尺码': '38'}",
     *         "{'颜色': '黑色', '尺码': '37'}",
     *         "{'颜色': '黑色', '尺码': '39'}",
     *         "{'颜色': '黑色', '尺码': '36'}",
     *         "{'颜色': '蓝色'}",
     *         "{'颜色': '黑色', '尺码': '35'}",
     *         "{'颜色': '蓝色', '尺码': '27'}"
     *     ]
     *
     */
    public Map<String, Set<String>> formatSpec(List<String> specList) {
        Map<String, Set<String>> resultMap = new HashMap<>();
        if (specList != null && specList.size() > 0) {
            for (String specJsonString : specList) {
                //将json数据转换为map
                Map<String, String> specMap = JSON.parseObject(specJsonString, Map.class);
                for (String specKey : specMap.keySet()) {
                    Set<String> specSet = resultMap.get(specKey);
                    if(specSet == null) {
                        specSet = new HashSet<String>();
                    }
                    //将规格的值放入set中
                    specSet.add(specMap.get(specKey));
                    //将set放入map中
                    resultMap.put(specKey,specSet);
                }
            }
        }
        return resultMap;
    }
}
