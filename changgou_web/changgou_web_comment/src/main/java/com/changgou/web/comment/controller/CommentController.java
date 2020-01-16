package com.changgou.web.comment.controller;

import com.changgou.comment.feign.CommentFeign;
import com.changgou.comment.pojo.Comment;
import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2020/1/15 19:20
 */
@Controller
@RequestMapping("/wcomment")
public class CommentController {

    @Autowired
    private CommentFeign commentFeign;

    @GetMapping("/{id}")
    @ResponseBody
    public int count(@PathVariable("id") String skuId){
        Integer count = commentFeign.count(skuId).getData();
        return count;
    }

    /**
     * 4.分别查询总评价数,中好差评数量
     * @param skuId
     * @return
     */
    @GetMapping("/kind/{id}")
    @ResponseBody
    public Result kindCount(@PathVariable("id") String skuId){
        Map<String, Long> data = commentFeign.kindCount(skuId).getData();
        return new Result(true, StatusCode.OK,"查询成功",data);
    }

    @PostMapping("/page/{pageNumber}/{pageSize}")
    @ResponseBody
    public Result list(@RequestBody Map<String,String> searchMap, @PathVariable("pageNumber")Integer pageNumber, @PathVariable("pageSize")Integer pageSize){
        PageResult<Comment> pageResult = commentFeign.list(searchMap, pageNumber, pageSize).getData();
        List<Comment> commentList = pageResult.getRows();
        return new Result(true,StatusCode.OK,"查询成功",commentList);
    }
}
