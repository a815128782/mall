package com.changgou.comment.feign;

import com.changgou.comment.pojo.Comment;
import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2020/1/15 19:13
 */
@FeignClient(name = "comment")
public interface CommentFeign {

    @PostMapping("/comment/{id}")
    public Result add(@RequestBody Comment comment,@PathVariable("id")String orderId);

    @GetMapping("/comment/count/{id}")
    public Result<Integer> count(@PathVariable("id")String skuId);

    /**
     * 4.分别查询总评价数,中好差评数量
     * @param skuId
     * @return
     */
    @GetMapping("/comment/kind/{id}")
    public Result<Map<String,Long>> kindCount(@PathVariable("id") String skuId);

    /**
     * 2.根据skuId查询评价列表
     * 分页.多条件
     * @param searchMap
     * @return
     */
    @PostMapping("/comment/page/{pageNumber}/{pageSize}")
    public Result<PageResult<Comment>> list(@RequestBody Map<String,String> searchMap, @PathVariable("pageNumber")Integer pageNumber, @PathVariable("pageSize")Integer pageSize);
}
