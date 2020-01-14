package com.changgou.comment.controller;

import com.changgou.comment.config.TokenDecode;
import com.changgou.comment.pojo.Comment;
import com.changgou.comment.service.CommentService;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.common.exception.ExceptionCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2020/1/13 15:17
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 1. 新增评价
     * @param comment
     * @return
     */
    @PostMapping("/{id}")
    public Result add(@RequestBody Comment comment,@PathVariable("id")String orderId){
        commentService.save(comment,orderId);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 2.根据skuId查询评价列表
     * @param skuId
     * @return
     */
    @GetMapping("/list/{skuId}")
    public Result list(@PathVariable("skuId")String skuId){
        List<Comment> commentList = commentService.findBySkuId(skuId);
        return new Result(true,StatusCode.OK,"查询成功",commentList);
    }


}
