package com.changgou.user.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.user.dao.CollectMapper;
import com.changgou.user.dao.FootMarkMapper;
import com.changgou.user.pojo.Collect;
import com.changgou.user.pojo.Footmark;
import com.changgou.user.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:sss
 * @Date:2020/1/13 15:52
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private FootMarkMapper footMarkMapper;

    @Autowired
    private SkuFeign skuFeign;

    //添加收藏
    @Override
    public void add(String username, String skuId) {

        Collect collect = new Collect();
        collect.setSkuId(skuId);
        collect.setUsername(username);
        collectMapper.insertSelective(collect);
    }

    /**
     * 添加足迹
     * @param username
     * @param skuId
     */
    @Override
    public void addFootMark(String username, String skuId) {
        Footmark footmark = new Footmark();
        footmark.setSkuId(skuId);
        footmark.setUsername(username);
        footMarkMapper.insertSelective(footmark);
    }

    /**
     * 收藏列表
     * @param username
     * @return
     */
    @Override
    public List<Sku> list(String username) {

        List list= new ArrayList();
        Collect collect = new Collect();
        collect.setUsername(username);

        //查找
        Example example = new Example(Collect.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        List<Collect> collectList = collectMapper.selectByExample(example);

        for (Collect c : collectList) {
            Sku sku = skuFeign.findById(c.getSkuId()).getData();
            list.add(sku);
        }

        return list;
    }

    /**
     * 足迹列表
     * @param username
     * @return
     */
    @Override
    public List<Sku> list2FootMark(String username) {

        List<Sku> list= new ArrayList();
        Footmark footmark = new Footmark();
        footmark.setUsername(username);

        //查找
        Example example = new Example(Collect.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        List<Footmark> footMarkList = footMarkMapper.selectByExample(example);

        for (Footmark c : footMarkList) {
            Sku sku = skuFeign.findById(c.getSkuId()).getData();
            list.add(sku);
        }

        return list;

    }
}
