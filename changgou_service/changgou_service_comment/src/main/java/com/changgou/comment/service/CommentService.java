package com.changgou.comment.service;

import com.changgou.comment.pojo.Comment;

import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2020/1/13 15:18
 */
public interface CommentService {
    /**
     * 1. 新增
     * @param comment
     * @param orderId
     */
    void save(Comment comment, String orderId);

    /**
     * 2.根据skuId查询评价列表
     * @param skuId
     * @return
     */
    List<Comment> findBySkuId(String skuId);
}
