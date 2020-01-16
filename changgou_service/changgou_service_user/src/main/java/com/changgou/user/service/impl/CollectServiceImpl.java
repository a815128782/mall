package com.changgou.user.service.impl;

import com.changgou.common.util.IdWorker;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Log;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.user.dao.CollectMapper;
import com.changgou.user.dao.FootMarkMapper;
import com.changgou.user.pojo.Collect;
import com.changgou.user.pojo.Footmark;
import com.changgou.user.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private SpuFeign spuFeign;
    @Autowired
    private IdWorker idWorker;

    //添加收藏
    @Override
    public void add(String username, String skuId) {

        Collect collect = new Collect();
        /*long id = idWorker.nextId();
        System.out.println("这个收藏类的id是"+id);
        collect.setId(id);*/
        collect.setSkuId(skuId);
        collect.setUsername(username);
        collectMapper.insert(collect);
    }

    /**
     * 添加足迹
     * @param username
     * @param skuId
     */
    @Override
    public void addFootMark(String username, String skuId) {
        Footmark footmark = new Footmark();
        footmark.setSku_id(skuId);
        footmark.setUsername(username);
        footmark.setCreate_time(new Date());
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
        List<Collect> collectList = collectMapper.findCollectList(username);
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
        Example example = new Example(Footmark.class);
        example.orderBy("create_time").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        List<Footmark> footMarkList = footMarkMapper.selectByExample(example);

        for (Footmark c : footMarkList) {
            Sku sku = skuFeign.findById(c.getSku_id()).getData();
            list.add(sku);
        }

        return list;

    }

    /**
     * 根据id查询收藏记录
     * @param id
     */
    @Override
    public List<Collect> findBySkuId(String id) {
        Example example = new Example(Collect.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sku_id",id);
        List<Collect> collects = collectMapper.selectByExample(example);
        return collects;
    }

    /**
     * 查询足迹记录
     * @param id
     * @return
     */
    @Override
    public List<Footmark> findSkuById(String id) {
        Example example = new Example(Footmark.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sku_id",id);
        List<Footmark> footmarks = footMarkMapper.selectByExample(example);

        return footmarks;
    }

    /**
     * 根据id删除足迹
     *
     * @param id
     */
    @Override
    public void deleteFootMark(String id) {
        Example example = new Example(Footmark.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sku_id", id);
        footMarkMapper.deleteByExample(example);
    }

    /**
     * 删除收藏
     * @param id
     */
    @Override
    public void deleteCollect(String id) {
        Example example = new Example(Collect.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sku_id", id);
        collectMapper.deleteByExample(example);
    }
}
