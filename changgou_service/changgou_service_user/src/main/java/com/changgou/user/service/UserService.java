package com.changgou.user.service;

import com.changgou.common.entity.Result;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.Task;
import com.changgou.user.pojo.Areas;
import com.changgou.user.pojo.Center;
import com.changgou.user.pojo.Cities;
import com.changgou.user.pojo.User;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface UserService {

    /***
     * 查询所有
     * @return
     */
    List<User> findAll();

    /**
     * 根据ID查询
     * @param username
     * @return
     */
    User findById(String username);

    /***
     * 新增
     * @param user
     * @param smsCode
     */
    void add(String smsCode,User user);

    /***
     * 修改
     * @param user
     */
    void update(User user);

    /***
     * 删除
     * @param username
     */
    void delete(String username);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<User> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<User> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<User> findPage(Map<String, Object> searchMap, int page, int size);


//    int decrUserPoints(String username,Integer points);

    /**
     * 更新用户积分
     * @param task
     * @return
     */
    int updateUserPoint(Task task);

    /*
    * 查询个人信息
    * @param username
    * @return
    * */
    Center findCenter(String username);


    /***
     * 新增个人信息
     * @param username
     */
    void addCenter(String username);


    /*
     * 查询城市列表
     * @param username
     * @return
     * */
    List<Cities> findCitiesList(String province);

    /*
     * 查询区列表
     * @param username
     * @return
     * */
    List<Areas> findAreasList(String city);

    /*
     * 修改个人信息
     * @param center
     * */
    Integer updateCenter(Center center);

}
