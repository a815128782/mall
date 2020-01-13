package com.changgou.page.service;

/**
 * @author LiXiang
 */
public interface PageService {

    //生成静态化页面
    void generateHTML(String spuId);

    //删除静态化页面
    void delHTML(String spuId);
}
