package com.changgou.comment.service.impl;

import com.changgou.comment.config.TokenDecode;
import com.changgou.comment.pojo.Comment;
import com.changgou.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    /**
     * 1. 新增
     * @param comment
     * @param orderId
     */
    @Override
    public void save(Comment comment, String orderId) {
        // 1.获取用户信息
        String username = tokenDecode.getUserInfo().get("username");
        comment.setUsername(username);
        comment.setCommentDate(new Date());
        mongoTemplate.save(comment);
    }

    /**
     * 2.根据skuId查询评价列表
     * @param skuId
     * @return
     */
    @Override
    public List<Comment> findBySkuId(String skuId) {
        Query query = new Query(Criteria.where("skuId").is(skuId));
        List<Comment> commentList = mongoTemplate.find(query, Comment.class, COLLECTION_NAME);
        return commentList;
    }
}
