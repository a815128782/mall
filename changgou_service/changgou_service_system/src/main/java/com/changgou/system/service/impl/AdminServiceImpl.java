package com.changgou.system.service.impl;

import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.system.SystemCode;
import com.changgou.system.dao.AdminMapper;
import com.changgou.system.pojo.Admin;
import com.changgou.system.service.AdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Admin> findAll() {
        return adminMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Admin findById(Integer id){
        return  adminMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param admin
     */
    @Override
    public void add(Admin admin){
        if (admin == null) {
            ExceptionCast.cast(SystemCode.SYSTEM_ADD_ADMIN_ERROR);
        }
        //获取用户输入的登录名进行校验,查询是否重复
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loginName", admin.getLoginName());
        List<Admin> admins = adminMapper.selectByExample(example);
        if (admins.size() > 0){
            ExceptionCast.cast(SystemCode.SYSTEM_ADD_USERNAME_ERROR);
        }

        //获取密码加密的盐
        String salt = BCrypt.gensalt();

        //获取用户设置的密码
        String password = admin.getPassword();

        //加密
        String newPassword = BCrypt.hashpw(password, salt);

        //封装
        admin.setPassword(newPassword);

        //添加
        adminMapper.insert(admin);
    }


    /**
     * 修改
     * @param admin
     */
    @Override
    public void update(Admin admin){
        adminMapper.updateByPrimaryKey(admin);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Integer id){
        adminMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Admin> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return adminMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Admin> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Admin>)adminMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Admin> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Admin>)adminMapper.selectByExample(example);
    }

    @Override
    public boolean login(Admin admin) {
        if(StringUtils.isBlank(admin.getLoginName())){
            ExceptionCast.cast(SystemCode.SYSTEM_LOGIN_USERNAME_ERROR);
        }
        if(StringUtils.isBlank(admin.getPassword())){
            ExceptionCast.cast(SystemCode.SYSTEM_LOGIN_PASSWORD_ERROR);
        }

        //根据管理员信息，获取数据库中的管理员信息
        Admin admin1 = new Admin();
        admin1.setLoginName(admin.getLoginName());
        admin1.setStatus("1");
        Admin dataAdmin = adminMapper.selectOne(admin1);

        if (dataAdmin == null){
            ExceptionCast.cast(SystemCode.SYSTEM_LOGIN_ERROR);
        }

        //获取密码,进行密码校验
        String dataAdminPassword = dataAdmin.getPassword();
        String loginPassword = admin.getPassword();

        if(!BCrypt.checkpw(loginPassword, dataAdminPassword)){
            ExceptionCast.cast(SystemCode.SYSTEM_LOGIN_ERROR);
        }
        return true;
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 用户名
            if(searchMap.get("loginName")!=null && !"".equals(searchMap.get("loginName"))){
                criteria.andLike("loginName","%"+searchMap.get("loginName")+"%");
           	}
            // 密码
            if(searchMap.get("password")!=null && !"".equals(searchMap.get("password"))){
                criteria.andLike("password","%"+searchMap.get("password")+"%");
           	}
            // 状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andEqualTo("status",searchMap.get("status"));
           	}

            // id
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }

        }
        return example;
    }

}
