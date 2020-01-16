package com.changgou.comment.controller;

import com.changgou.comment.config.TokenDecode;
import com.changgou.comment.pojo.Comment;
import com.changgou.comment.service.CommentService;
import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.common.exception.ExceptionCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2020/1/13 15:17
 */
@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 1. 新增评价
     * @param
     * @return
     */
    @PostMapping("/{id}")
    public Result add(@RequestBody Comment comment,@PathVariable("id")String orderId){
        commentService.save(comment,orderId);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 2.根据skuId查询评价列表
     * 分页.多条件
     * @param searchMap
     * @return
     */
    @PostMapping("/page/{pageNumber}/{pageSize}")
    public Result list(@RequestBody Map<String,String> searchMap,@PathVariable("pageNumber")Integer pageNumber,@PathVariable("pageSize")Integer pageSize){
        PageResult<Comment> commentList = commentService.findList(searchMap,pageNumber,pageSize);

        return new Result(true,StatusCode.OK,"查询成功",commentList);
    }

    /**
     * 3.查询商品总评价数,并存入Redis
     * @return
     */
    @GetMapping
    public Result AutoCount(){
       commentService.AutoCount();
       return new Result(true,StatusCode.OK,"商品总评价数更新成功");
    }

    /**
     * 4.分别查询总评价数,中好差评数量
     * @param skuId
     * @return
     */
    @GetMapping("/kind/{id}")
    public Result kindCount(@PathVariable("id") String skuId){
        Map<String,Long> map = commentService.kindCount(skuId);
        return new Result(true,StatusCode.OK,"查询成功",map);
    }

    /**]
     * 查询评价数量
     * @param skuId
     * @return
     */
    @GetMapping("/count/{id}")
    public Result count(@PathVariable("id")String skuId){
        Integer count = commentService.count(skuId);
        return new Result(true,StatusCode.OK,"查询成功",count);
    }

}
