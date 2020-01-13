package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.goods.GoodsCode;
import com.changgou.common.util.IdWorker;
import com.changgou.goods.dao.*;
import com.changgou.goods.pojo.*;
import com.changgou.goods.service.SpuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    /**
     * 查询全部列表
     *
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    /**
     * @param goods 封装 spu sku的实体类
     */
    @Override
    public void add(Goods goods) {
        //1.添加spu
        Spu spu = goods.getSpu();
        //设置分布式id
        long spuId = idWorker.nextId();
        spu.setId(String.valueOf(spuId));
        //设置删除状态 0 未删除
        spu.setIsDelete("0");
        //设置上架状态 0未上架 1 上架
        spu.setIsMarketable("0");
        //设置审核状态 0 未审核
        spu.setStatus("0");
        //设置是否启用规格
        if(!"1.".equals(spu.getIsEnableSpec())) {
            spu.setIsEnableSpec("1");
        }
        spuMapper.insertSelective(spu);

        if ("1".equals(spu.getIsEnableSpec())) {
            //2.添加sku集合
            this.saveSkuList(goods);
        } else{
            ExceptionCast.cast(GoodsCode.GOODS_SPU_SPEC_ERROR);
        }
    }

    /**
     * 添加sku集合数据
     *
     * @param goods
     */
    private void saveSkuList(Goods goods) {
        Spu spu = goods.getSpu();
        //查询分类对象
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        //查询品牌对象
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());

        //设置品牌和分类的关联关系
        //查询关联表
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setCategoryId(spu.getCategory3Id());
        categoryBrand.setBrandId(spu.getBrandId());
        int count = categoryBrandMapper.selectCount(categoryBrand);
        if (count == 0) {
            //品牌与分类还没有关联关系
            categoryBrandMapper.insert(categoryBrand);
        }
        //1.获取sku集合
        List<Sku> skuList = goods.getSkuList();
        if (skuList != null) {
            //遍历sku集合,循环填充数据并添加到数据库中
            for (Sku sku : skuList) {
                //设置skuId
                sku.setId(String.valueOf(idWorker.nextId()));
                //设置sku规格数据  如果为空,设置:{}
                if (StringUtils.isEmpty(sku.getSpec())) {
                    sku.setSpec("{}");
                }
                //设置sku名称(spu名称+规格)
                String name = spu.getName();
                //将规格的json转换为Map,将Map中的value进行名称的拼接
                Map<String, String> specMap = JSON.parseObject(sku.getSpec(), Map.class);
                if (specMap != null && specMap.size() > 0) {
                    for (String value : specMap.values()) {
                        name += " " + value;
                    }
                }
                sku.setName(name);
                //设置spuId
                sku.setSpuId(spu.getId());
                //设置创建与修改时间
                sku.setCreateTime(new Date());
                sku.setUpdateTime(new Date());
                //设置分类id
                sku.setCategoryId(category.getId());
                //设置商品分类的名称
                sku.setCategoryName(category.getName());
                //设置品牌名称
                sku.setBrandName(brand.getName());
                //将sku添加到数据库
                skuMapper.insertSelective(sku);
            }
        }
    }

    /**
     * 添加sku数据
     *
     * @param goods
     */
//    private void saveSku(Goods goods) {
//        Spu spu = goods.getSpu();
//
//        //查询分类对象
//        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
//        //查询品牌对象
//        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
//
//        //设置品牌和分类的关联关系
//        //查询关联表
//        CategoryBrand categoryBrand = new CategoryBrand();
//        categoryBrand.setCategoryId(spu.getCategory3Id());
//        categoryBrand.setBrandId(spu.getBrandId());
//        int count = categoryBrandMapper.selectCount(categoryBrand);
//        if (count == 0) {
//            //品牌与分类还没有关联关系
//            categoryBrandMapper.insert(categoryBrand);
//        }
//        //1.获取sku
//        Sku sku = new Sku();
//        //设置skuId
//        sku.setId(String.valueOf(idWorker.nextId()));
//        //设置sku规格数据  如果为空,设置:{}
//        if (StringUtils.isEmpty(sku.getSpec())) {
//            sku.setSpec("{}");
//        }
//        //设置sku名称(spu名称+规格)
//        /*String name = spu.getName();
//        //将规格的json转换为Map,将Map中的value进行名称的拼接
//        Map<String, String> specMap = JSON.parseObject(sku.getSpec(), Map.class);
//        if (specMap != null && specMap.size() > 0) {
//            for (String value : specMap.values()) {
//                name += " " + value;
//            }
//        }*/
//        sku.setName(spu.getName());
//        //设置skuSn
//        sku.setSn(spu.getSn());
//        //设置spuId
//        sku.setSpuId(spu.getId());
//        //设置创建与修改时间
//        sku.setCreateTime(new Date());
//        sku.setUpdateTime(new Date());
//        //设置分类id
//        sku.setCategoryId(category.getId());
//        //设置商品分类的名称
//        sku.setCategoryName(category.getName());
//        //设置品牌名称
//        sku.setBrandName(brand.getName());
//        //将sku添加到数据库
//        skuMapper.insertSelective(sku);
//    }


    /**
     * 修改
     *
     * @param goods
     */
    @Override
    public void update(Goods goods) {
        //修改spu
        Spu spu = goods.getSpu();
        spuMapper.updateByPrimaryKey(spu);

        //修改sku
        List<Sku> skuList = goods.getSkuList();
        //删除原有的sku信息
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", spu.getId());
        skuMapper.deleteByExample(example);
        //重新添加
        this.saveSkuList(goods);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        //逻辑删除
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断当前商品是否处于下架状态
        if (!"0".equals(spu.getIsMarketable())) {
            ExceptionCast.cast(GoodsCode.GOODS_PULL_ERROR);
        }
        //如果是下架状态,修改对应的标记位0,进行逻辑删除
        spu.setIsDelete("1");
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }


    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<Spu> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spu> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Spu>) spuMapper.selectAll();
    }

    /**
     * 条件+分页查询
     *
     * @param searchMap 查询条件
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @Override
    public Page<Spu> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        return (Page<Spu>) spuMapper.selectByExample(example);
    }

    /**
     * 根据ID查询spu和sku列表
     *
     * @param id
     * @return
     */
    @Override
    public Goods findGoodsById(String id) {
        Goods goods = new Goods();

        //查询spu信息,封装到goods中
        Spu spu = spuMapper.selectByPrimaryKey(id);
        goods.setSpu(spu);

        //查询sku集合,封装到goods中
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        //根据spu进行sku列表的查询
        criteria.andEqualTo("spuId", id);
        List<Sku> skuList = skuMapper.selectByExample(example);
        goods.setSkuList(skuList);
        return goods;
    }

    /**
     * 物理删除
     *
     * @param id 商品 id
     */
    @Override
    public void readDel(String id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断当前商品必须处于已删除状态
        if (!"1".equals(spu.getIsDelete())) {
            ExceptionCast.cast(GoodsCode.GOODS_RESTOR_ERROR);
        }
        //删除
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 还原商品
     *
     * @param id
     */
    @Override
    public void restore(String id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断当前商品必须处于已删除状态
        if (!"1".equals(spu.getIsDelete())) {
            ExceptionCast.cast(GoodsCode.GOODS_RESTOR_ERROR);
        }
        //修改并保存
        spu.setIsDelete("0");
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品上架
     *
     * @param id
     */
    @Override
    public void put(String id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            ExceptionCast.cast(GoodsCode.GOODS_NE_ERROR);
        }
        //判断当前商品是否处于删除状态
        if ("1".equals(spu.getIsDelete())) {
            ExceptionCast.cast(GoodsCode.GOODS_DELETE_ERROR);
        }
        //判断当前商品是否已审核
        if (!"1".equals(spu.getStatus())) {
            ExceptionCast.cast(GoodsCode.GOODS_CHECK_ERROR);
        }
        //商品处于未删除状态的话,则修改上下架状态为已上架(1)
        spu.setIsMarketable("1");
        //保存修改
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品下架
     *
     * @param id
     */
    @Override
    public void pull(String id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断当前商品是否处于删除状态
        if ("1".equals(spu.getIsDelete())) {
            ExceptionCast.cast(GoodsCode.GOODS_DELETE_ERROR);
        }
        //商品处于未删除状态的话,则修改上下架状态位已下架(0)
        spu.setIsMarketable("0");
        //修改
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 审核
     *
     * @param id spuId
     */
    @Override
    public void audit(String id) {
        //查询spu对象
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            ExceptionCast.cast(GoodsCode.GOODS_NE_ERROR);
        }
        //判断当前spu是否处于删除状态
        if ("1".equals(spu.getIsDelete())) {
            ExceptionCast.cast(GoodsCode.GOODS_DELETE_ERROR);
        }
        //不处于删除状态,修改审核状态为 1 ,上下架状态也为 1
        spu.setStatus("1");
        spu.setIsMarketable("1");
        //执行修该
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 构建查询对象
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 主键
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andEqualTo("id", searchMap.get("id"));
            }
            // 货号
            if (searchMap.get("sn") != null && !"".equals(searchMap.get("sn"))) {
                criteria.andEqualTo("sn", searchMap.get("sn"));
            }
            // SPU名
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            // 副标题
            if (searchMap.get("caption") != null && !"".equals(searchMap.get("caption"))) {
                criteria.andLike("caption", "%" + searchMap.get("caption") + "%");
            }
            // 图片
            if (searchMap.get("image") != null && !"".equals(searchMap.get("image"))) {
                criteria.andLike("image", "%" + searchMap.get("image") + "%");
            }
            // 图片列表
            if (searchMap.get("images") != null && !"".equals(searchMap.get("images"))) {
                criteria.andLike("images", "%" + searchMap.get("images") + "%");
            }
            // 售后服务
            if (searchMap.get("saleService") != null && !"".equals(searchMap.get("saleService"))) {
                criteria.andLike("saleService", "%" + searchMap.get("saleService") + "%");
            }
            // 介绍
            if (searchMap.get("introduction") != null && !"".equals(searchMap.get("introduction"))) {
                criteria.andLike("introduction", "%" + searchMap.get("introduction") + "%");
            }
            // 规格列表
            if (searchMap.get("specItems") != null && !"".equals(searchMap.get("specItems"))) {
                criteria.andLike("specItems", "%" + searchMap.get("specItems") + "%");
            }
            // 参数列表
            if (searchMap.get("paraItems") != null && !"".equals(searchMap.get("paraItems"))) {
                criteria.andLike("paraItems", "%" + searchMap.get("paraItems") + "%");
            }
            // 是否上架
            if (searchMap.get("isMarketable") != null && !"".equals(searchMap.get("isMarketable"))) {
                criteria.andEqualTo("isMarketable", searchMap.get("isMarketable"));
            }
            // 是否启用规格
            if (searchMap.get("isEnableSpec") != null && !"".equals(searchMap.get("isEnableSpec"))) {
                criteria.andEqualTo("isEnableSpec", searchMap.get("isEnableSpec"));
            }
            // 是否删除
            if (searchMap.get("isDelete") != null && !"".equals(searchMap.get("isDelete"))) {
                criteria.andEqualTo("isDelete", searchMap.get("isDelete"));
            }
            // 审核状态
            if (searchMap.get("status") != null && !"".equals(searchMap.get("status"))) {
                criteria.andEqualTo("status", searchMap.get("status"));
            }

            // 品牌ID
            if (searchMap.get("brandId") != null) {
                criteria.andEqualTo("brandId", searchMap.get("brandId"));
            }
            // 一级分类
            if (searchMap.get("category1Id") != null) {
                criteria.andEqualTo("category1Id", searchMap.get("category1Id"));
            }
            // 二级分类
            if (searchMap.get("category2Id") != null) {
                criteria.andEqualTo("category2Id", searchMap.get("category2Id"));
            }
            // 三级分类
            if (searchMap.get("category3Id") != null) {
                criteria.andEqualTo("category3Id", searchMap.get("category3Id"));
            }
            // 模板ID
            if (searchMap.get("templateId") != null) {
                criteria.andEqualTo("templateId", searchMap.get("templateId"));
            }
            // 运费模板id
            if (searchMap.get("freightId") != null) {
                criteria.andEqualTo("freightId", searchMap.get("freightId"));
            }
            // 销量
            if (searchMap.get("saleNum") != null) {
                criteria.andEqualTo("saleNum", searchMap.get("saleNum"));
            }
            // 评论数
            if (searchMap.get("commentNum") != null) {
                criteria.andEqualTo("commentNum", searchMap.get("commentNum"));
            }

        }
        return example;
    }


}
