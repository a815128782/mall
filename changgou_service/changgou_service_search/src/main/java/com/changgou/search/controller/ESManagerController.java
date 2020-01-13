package com.changgou.search.controller;

import com.changgou.common.entity.R;
import com.changgou.common.entity.Result;
import com.changgou.search.service.ESManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiXiang
 */
@RestController
@RequestMapping("/manager")
public class ESManagerController {
    @Autowired
    private ESManagerService esManagerService;

    /**
     * 创建索引库结构
     * @return Result
     */
    @GetMapping("/create")
    public Result create(){
        esManagerService.createMappingAndIndex();
        return R.T("索引库结构创建");
    }

    /**
     * 导入全部数据
     * @return Result
     */
    @GetMapping("/import")
    public Result importAll() {
        esManagerService.importAll();
        return R.T("导入全部数据");
    }


    @GetMapping("/import/page")
    public Result importPageAll() {
        esManagerService.importPageAll();
        return R.T("分页导入全部数据");
    }
}
