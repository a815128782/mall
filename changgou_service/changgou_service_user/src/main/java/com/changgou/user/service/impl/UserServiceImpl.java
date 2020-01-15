package com.changgou.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.common.entity.Result;
import com.changgou.order.pojo.Order;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.user.UserCode;
import com.changgou.common.util.ValidateCodeUtils;
import com.changgou.order.pojo.Task;
import com.changgou.user.dao.*;
import com.changgou.user.pojo.*;
import com.changgou.user.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    public static final String VALIDATECODE="validateCode_";
    private static final String CITY = "city_";
    private static final String AREA = "area_";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    PointLogMapper pointLogMapper;
    @Autowired
    CenterMapper centerMapper;
    @Autowired
    CitiesMapper citiesMapper;
    @Autowired
    ProvincesMapper provincesMapper;
    @Autowired
    AreasMapper areasMapper;
    @Autowired
    OccupationsMapper occupationsMapper;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 查询全部列表
     *
     * @return
     */
    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param username
     * @return
     */
    @Override
    public User findById(String username) {
        return userMapper.selectByPrimaryKey(username);
    }


    /**
     * 增加
     * @param user
     */
    @Override
    public void add(String smsCode,User user){
        String validateCode =  redisTemplate.boundValueOps(VALIDATECODE + user.getPhone()).get()+"";
        if(!smsCode.equals(validateCode)){
            ExceptionCast.cast(UserCode.USER_VALIDATECODE_ERROR);
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setCreated(new Date());
        user.setUpdated(new Date());
        user.setSourceType("1");
        userMapper.insertSelective(user);
    }


    /**
     * 修改
     *
     * @param user
     */
    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 删除
     *
     * @param username
     */
    @Override
    public void delete(String username) {
        userMapper.deleteByPrimaryKey(username);
    }


    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<User> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return userMapper.selectByExample(example);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<User> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<User>) userMapper.selectAll();
    }

    /*@Override
    public int decrUserPoints(String username, Integer points) {
        return userMapper.decrUserPoints(username,points);
    }*/

    @Override
    @Transactional
    public int updateUserPoint(Task task) {
        System.out.println("用户服务现在开始对任务进行处理");
        //1.从task中获取相关数据
        Map map = JSON.parseObject(task.getRequestBody(), Map.class);
        String username = map.get("username").toString();
        String orderId = map.get("orderId").toString();
        int point = (int) map.get("point");

        //2.判断当前的任务是否操作过
        PointLog pointLog = pointLogMapper.findPointLogByOrderId(orderId);
        if (pointLog != null) {
            return 0;
        }

        //3.将任务存入到redis中
        redisTemplate.boundValueOps(task.getId()).set("exist", 30, TimeUnit.SECONDS);

        //4.修改用户积分
        int result = userMapper.updateUserPoints(username, point);
        if (result <= 0) {
            return 0;
        }

        //5.记录积分日志信息
        pointLog = new PointLog();
        pointLog.setUserId(username);
        pointLog.setOrderId(orderId);
        pointLog.setPoint(point);
        result = pointLogMapper.insertSelective(pointLog);
        if (result <= 0) {
            return 0;
        }

        //6.删除redis中的任务信息
        redisTemplate.delete(task.getId());
        System.out.println("用户服务完成了更改用户积分的操作");
        return 1;
    }

    /*
     * 查询个人信息
     * @param username
     * @return
     * */
    @Override
    public Center findCenter(String username) {
        Center center = centerMapper.selectByPrimaryKey(username);

        List<Cities> citiesList = new ArrayList<>();
        List<Provinces> provincesList = new ArrayList<>();
        List<Areas> areasList = new ArrayList<>();
        List<Occupations> occupationsList = new ArrayList<>();
        if (center==null){
            addCenter(username);
            //查询所以省份
            provincesList = (List<Provinces>)redisTemplate.boundValueOps("provincesList").get();
            if (provincesList == null || provincesList.size()<=0){
                provincesList = provincesMapper.selectAll();
                redisTemplate.boundValueOps("provincesList").set(provincesList);
            }

            //查询省份对应的市
            citiesList = (List<Cities>)redisTemplate.boundValueOps(CITY+provincesList.get(0).getProvinceid()).get();
            if (citiesList == null || citiesList.size()<=0){
                addCitiesList(provincesList);
                citiesList = (List<Cities>)redisTemplate.boundValueOps(CITY+provincesList.get(0).getProvinceid()).get();
            }

            areasList = (List<Areas>)redisTemplate.boundHashOps(AREA+citiesList.get(0).getProvinceid()).get(citiesList.get(0).getCityid());
            if (areasList == null || areasList.size()<=0){
                addAreasList(provincesList);
                areasList = (List<Areas>)redisTemplate.boundHashOps(AREA+citiesList.get(0).getProvinceid()).get(citiesList.get(0).getCityid());
            }

            occupationsList = (List<Occupations>)redisTemplate.boundValueOps("occupationsList").get();
            if (occupationsList == null || occupationsList.size()<=0){
                occupationsList = occupationsMapper.selectAll();
                redisTemplate.boundValueOps("occupationsList").set(occupationsList);
            }
        }else {
            provincesList = (List<Provinces>)redisTemplate.boundValueOps("provincesList").get();
            if (provincesList == null || provincesList.size()<=0){
                provincesList = provincesMapper.selectAll();
                redisTemplate.boundValueOps("provincesList").set(provincesList);
            }

            //查询省份对应的市
            Provinces provinces = findProvinces(center.getProvince());
            citiesList = (List<Cities>)redisTemplate.boundValueOps(CITY+provinces.getProvinceid()).get();
            if (citiesList == null || citiesList.size()<=0){
                addCitiesList(provincesList);
                citiesList = (List<Cities>)redisTemplate.boundValueOps(CITY+provinces.getProvinceid()).get();
            }

            Cities cities = findCities(center.getCity());
            areasList = (List<Areas>)redisTemplate.boundHashOps(AREA+cities.getProvinceid()).get(cities.getCityid());
            if (areasList == null || areasList.size()<=0){
                addAreasList(provincesList);
                areasList = (List<Areas>)redisTemplate.boundHashOps(AREA+cities.getProvinceid()).get(cities.getCityid());
            }

            occupationsList = (List<Occupations>)redisTemplate.boundValueOps("occupationsList").get();
            if (occupationsList == null || occupationsList.size()<=0){
                occupationsList = occupationsMapper.selectAll();
                redisTemplate.boundValueOps("occupationsList").set(occupationsList);
            }
        }

        center.setCitiesList(citiesList);
        center.setProvincesList(provincesList);
        center.setAreasList(areasList);
        center.setOccupationsList(occupationsList);
        return center;
    }

    /***
     * 新增个人信息
     * @param username
     */
    @Override
    public void addCenter(String username) {
        Center center = new Center();
        center.setUsername(username);
        centerMapper.insertSelective(center);
    }

    /*
     * 查询城市列表
     * @param province
     * @return
     * */
    @Override
    public List<Cities> findCitiesList(String province) {
        Provinces provinces = findProvinces(province);
        List<Cities>citiesList = (List<Cities>)redisTemplate.boundValueOps(CITY+provinces.getProvinceid()).get();
        if (citiesList == null || citiesList.size()<=0){
            Example example = new Example(Cities.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("province",province);
            citiesList = citiesMapper.selectByExample(example);
        }
        return citiesList;
    }

    /*
     * 查询区列表
     * @param username
     * @return
     * */
    @Override
    public List<Areas> findAreasList(String city) {
        Cities cities = findCities(city);
        List<Areas> areasList = (List<Areas>)redisTemplate.boundHashOps(AREA+cities.getProvinceid()).get(cities.getCityid());
        if (areasList == null || areasList.size()<=0){
            Example example = new Example(Areas.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("city",city);
            areasList = areasMapper.selectByExample(example);
        }
        return areasList;
    }

    /*
     * 修改个人信息
     * @param center
     * */
    @Override
    public Integer updateCenter(Center center) {
        int i = centerMapper.updateByPrimaryKeySelective(center);
        return i;
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
    public Page<User> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        return (Page<User>) userMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 用户名
            if (searchMap.get("username") != null && !"".equals(searchMap.get("username"))) {
                criteria.andEqualTo("username", searchMap.get("username"));
            }
            // 密码，加密存储
            if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                criteria.andEqualTo("password", searchMap.get("password"));
            }
            // 注册手机号
            if (searchMap.get("phone") != null && !"".equals(searchMap.get("phone"))) {
                criteria.andLike("phone", "%" + searchMap.get("phone") + "%");
            }
            // 注册邮箱
            if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                criteria.andLike("email", "%" + searchMap.get("email") + "%");
            }
            // 会员来源：1:PC，2：H5，3：Android，4：IOS
            if (searchMap.get("sourceType") != null && !"".equals(searchMap.get("sourceType"))) {
                criteria.andEqualTo("sourceType", searchMap.get("sourceType"));
            }
            // 昵称
            if (searchMap.get("nickName") != null && !"".equals(searchMap.get("nickName"))) {
                criteria.andLike("nickName", "%" + searchMap.get("nickName") + "%");
            }
            // 真实姓名
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            // 使用状态（1正常 0非正常）
            if (searchMap.get("status") != null && !"".equals(searchMap.get("status"))) {
                criteria.andEqualTo("status", searchMap.get("status"));
            }
            // 头像地址
            if (searchMap.get("headPic") != null && !"".equals(searchMap.get("headPic"))) {
                criteria.andLike("headPic", "%" + searchMap.get("headPic") + "%");
            }
            // QQ号码
            if (searchMap.get("qq") != null && !"".equals(searchMap.get("qq"))) {
                criteria.andLike("qq", "%" + searchMap.get("qq") + "%");
            }
            // 手机是否验证 （0否  1是）
            if (searchMap.get("isMobileCheck") != null && !"".equals(searchMap.get("isMobileCheck"))) {
                criteria.andEqualTo("isMobileCheck", searchMap.get("isMobileCheck"));
            }
            // 邮箱是否检测（0否  1是）
            if (searchMap.get("isEmailCheck") != null && !"".equals(searchMap.get("isEmailCheck"))) {
                criteria.andEqualTo("isEmailCheck", searchMap.get("isEmailCheck"));
            }
            // 性别，1男，0女
            if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                criteria.andEqualTo("sex", searchMap.get("sex"));
            }

            // 会员等级
            if (searchMap.get("userLevel") != null) {
                criteria.andEqualTo("userLevel", searchMap.get("userLevel"));
            }
            // 积分
            if (searchMap.get("points") != null) {
                criteria.andEqualTo("points", searchMap.get("points"));
            }
            // 经验值
            if (searchMap.get("experienceValue") != null) {
                criteria.andEqualTo("experienceValue", searchMap.get("experienceValue"));
            }

        }
        return example;
    }

    public static void main(String[] args) {
        String gensalt = BCrypt.gensalt();
        String password = BCrypt.hashpw("123456", gensalt);
        System.out.println(password);
    }


    /***
     * redis 添加城市
     * @param provincesList
     */

    public void addCitiesList(List<Provinces> provincesList) {
        List<Cities> citiesList = new ArrayList<>();
        for (Provinces provinces : provincesList) {
            Example example = new Example(Cities.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("provinceid",provinces.getProvinceid());
            citiesList = citiesMapper.selectByExample(example);
            redisTemplate.boundValueOps(CITY+provinces.getProvinceid()).set(citiesList);
        }
    }

    /***
     * redis 添加区
     * @param provincesList
     */

    public void addAreasList(List<Provinces> provincesList) {
        List<Areas> areasList = new ArrayList<>();
        for (Provinces provinces : provincesList) {
            List<Cities>citiesList = (List<Cities>)redisTemplate.boundValueOps(CITY+provinces.getProvinceid()).get();
            for (Cities cities : citiesList) {
                Example example = new Example(Areas.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("cityid",cities.getCityid());
                areasList = areasMapper.selectByExample(example);
                redisTemplate.boundHashOps(AREA+cities.getProvinceid()).put(cities.getCityid(),areasList);
            }
        }
    }
    public Provinces findProvinces(String province){
        //查询省份
        Example example = new Example(Provinces.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("province",province);
        Provinces provinces = provincesMapper.selectOneByExample(example);
        return provinces;
    }

    public Cities findCities(String city){
        //查询市
        Example example1 = new Example(Cities.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("city",city);
        Cities cities=citiesMapper.selectOneByExample(example1);
        return cities;
    }
}
