package com.changgou.comment.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.comment.config.TokenDecode;
import com.changgou.comment.pojo.Comment;
import com.changgou.comment.service.CommentService;
import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.Result;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.comment.CommentCode;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.feign.OrderItemFeign;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author:Administrator
 * @Date: 2020/1/13 15:18
 */
@Service
public class CommentServiceImpl implements CommentService {
    public static final String COLLECTION_NAME = "comment"; // mongoDB数据库表名

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private OrderItemFeign orderItemFeign;

    @Autowired
    private OrderFeign orderFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 1. 新增
     * @param comment
     * @param orderId
     */
    @Override
   // @GlobalTransactional(name = "Comment_save")
    public void save(Comment comment, String orderId) {
        // 1.获取用户信息
       String username = tokenDecode.getUserInfo().get("username");
        // 公共信息
        comment.setUsername(username);
        comment.setOrderId(orderId);
        comment.setCommentDate(new Date());

        User user = userFeign.findUserInfo(username);
        comment.setUserLevel(user.getUserLevel());
        comment.setHeadPic(user.getHeadPic());
        // 对于每一个orderItem都给相同的评论
        Map map = new HashMap();
        map.put("orderId",orderId);
        List<OrderItem> orderItemList = orderItemFeign.findList(map).getData();
        if (orderItemList == null || orderItemList.size()<=0){
            ExceptionCast.cast(CommentCode.ORDERITEM_BLANK_ERROR);
        }
        for (OrderItem orderItem : orderItemList) {
            comment.setSkuId(orderItem.getSkuId());
            Sku sku = skuFeign.findById(orderItem.getSkuId()).getData();
            String spec = sku.getSpec();
            Map<String,String> specMap = JSON.parseObject(spec, Map.class);
            comment.setSpec(specMap);
            mongoTemplate.save(comment);
        }

        // 修改订单状态为已评论
        orderFeign.updateOrderCommentStatus(orderId);
    }

    /**
     *2. 多条件分页
     * @param searchMap
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public PageResult<Comment> findList(Map<String, String> searchMap, Integer pageNumber, Integer pageSize) {
        Query query = new Query();
        String skuId = searchMap.get("skuId");
        query.addCriteria(Criteria.where("skuId").is(skuId));
        // 中评好评差评
        String condition = searchMap.get("condition");
        if (condition == null || "0".equals(condition)){
            // 全部评论
        }
        if ("1".equals(condition)){ // 好评
            query.addCriteria(Criteria.where("commodityScore").is(5));
        }
        if ("2".equals(condition)){
            // 差评
            query.addCriteria(Criteria.where("commodityScore").is(1));
        }
        if ("3".equals(condition)){ // 中评
            query.addCriteria(Criteria.where("commodityScore").gte(2).lte(4));
        }

        query.with(new Sort(Sort.Direction.DESC,"commentDate"));
        Long totalCount = mongoTemplate.count(query,Comment.class);
        if (pageNumber < 1){
            pageNumber = 1;
        }
        if (pageSize < 1){
            pageSize = 10;
        }
        List<Comment> commentList = mongoTemplate.find(query.skip(pageNumber-1).limit(pageSize), Comment.class);
        return new PageResult<Comment>(totalCount,commentList);
    }

    /**
     * 3.自动更新商品总评价数
     * @return
     */
    @Override
    public void AutoCount() {
        // 1.根据skuId分组查询其总评价数
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("skuId").count().as("totalComment"));
        AggregationResults<Map> mapAggregationResults = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, Map.class);
        for (Iterator<Map> iterator = mapAggregationResults.iterator();iterator.hasNext();){
            Map map = iterator.next();
            System.out.println(JSON.toJSONString(map));
            // 2.存储到Redis中
            redisTemplate.boundHashOps("sku_comment").put((String)map.get("_id"),(Integer)map.get("totalComment"));
        }
    }

    /**
     * 查询评论数量
     * @param skuId
     * @return
     */
    @Override
    public Integer count(String skuId) {
        Integer totalComment = (Integer) redisTemplate.boundHashOps("sku_comment").get(skuId);
        return totalComment;
    }

    /**
     * 4.分别查询总评价数,中好差评数量
     * @param skuId
     * @return
     */
    @Override
    public Map<String, Long> kindCount(String skuId) {
        Map<String,Long> map = new HashMap<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("skuId").is(skuId));
        Long totalCount =  mongoTemplate.count(query, Comment.class);
        map.put("totalCount",totalCount);

        query = new Query();
        query.addCriteria(Criteria.where("skuId").is(skuId)).addCriteria(Criteria.where("commodityScore").is(5));
        Long goodCount =  mongoTemplate.count(query, Comment.class);
        map.put("goodCount",goodCount);

        query = new Query();
        query.addCriteria(Criteria.where("skuId").is(skuId)).addCriteria(Criteria.where("commodityScore").is(1));
        Long badCount =  mongoTemplate.count(query, Comment.class);
        map.put("badCount",badCount);

        Long commonCount = totalCount - goodCount -badCount;
        map.put("commonCount",commonCount);

        return map;
    }

    /**
     * 2.根据skuId查询评价列表
     * @param skuId
     * @return
     *//*
    @Override
    public Page<Comment> findBySkuId(Integer skuId) {
        Query query = new Query(Criteria.where("skuCommentList.commodityScore").is(skuId));
        Page<Comment> commentList = mongoTemplate.find(query, Comment.class, COLLECTION_NAME);
        return commentList;
    }*/
}
