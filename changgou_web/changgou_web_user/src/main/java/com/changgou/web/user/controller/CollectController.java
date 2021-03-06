package com.changgou.web.user.controller;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Sku;
import com.changgou.user.feign.UserFeign;
import org.omg.CORBA.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author:sss
 * @Date:2020/1/13 16:00
 */
@Controller
@RequestMapping("/wcollect")
public class CollectController {

    @Autowired
    private UserFeign userFeign;

    @RequestMapping("/add/{id}")
    @ResponseBody
    public Result add(@PathVariable("id") String id){
        Result result = userFeign.add(id);
        return result;
    }

    @GetMapping("/deleteCollect/{id}")
    public String deleteCollect(@PathVariable("id") String id,Model model) {
        Result result = userFeign.deleteCollect(id);
        List<Sku> list = userFeign.list().getData();
        model.addAttribute("list",list);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "center-collect";
    }

    /**
     * 添加足迹
     * @param id
     * @return
     */
    @RequestMapping("/addFootMark/{id}")
    @ResponseBody
    public Result addFootMark(@PathVariable("id") String id){
        Result result = userFeign.addFootMark(id);
        return result;
    }


    @RequestMapping("/list")
    public String list(Model model){
        List<Sku> list = userFeign.list().getData();
        model.addAttribute("list",list);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "center-collect";
    }

    /**
     * 足迹列表
     * @param model
     * @return
     */
    @RequestMapping("/list2FootMark")
    public String list2FootMark(Model model){
        List<Sku> list = userFeign.list2FootMark().getData();
        model.addAttribute("list",list);
        String username = (String) userFeign.getUsername().getData();
        model.addAttribute("username",username);
        return "center-footmark";
    }

    //删除足迹
    @GetMapping("/deleteFootMark/{id}")
    public Result deleteFootMark(@PathVariable("id") String id) {
        Result result = userFeign.deleteFootMark(id);
        return result;
    }

}
