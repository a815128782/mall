package com.changgou.comment.service;

import com.changgou.comment.pojo.Comment;
import com.changgou.common.entity.PageResult;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.Map;

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
    /*Page<Comment> findBySkuId(Integer skuId);*/

    /**
     * 2.多条件分页
     * @param searchMap
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PageResult<Comment> findList(Map<String, String> searchMap, Integer pageNumber, Integer pageSize);

    /**
     * 3.更新商品总评价数
     * @return
     */
    void AutoCount();

    /**
     * 查询评价数量
     * @param skuId
     * @return
     */
    Integer count(String skuId);

    /**
     * 4.分别查询总评价数,中好差评数量
     * @param skuId
     * @return
     */
    Map<String,Long> kindCount(String skuId);
}
