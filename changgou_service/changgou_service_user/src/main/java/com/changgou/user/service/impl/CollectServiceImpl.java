package com.changgou.user.service.impl;

import com.changgou.user.dao.CollectMapper;
import com.changgou.user.pojo.Collect;
import com.changgou.user.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:sss
 * @Date:2020/1/13 15:52
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    //添加收藏
    @Override
    public void add(String username, String skuId) {

        Collect collect = new Collect();
        collect.setSkuId(skuId);
        collect.setUsername(username);
        collectMapper.insert(collect);
    }
}
