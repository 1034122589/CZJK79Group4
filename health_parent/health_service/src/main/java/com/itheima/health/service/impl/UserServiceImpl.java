package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:48
 * @Version V1.0
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;


    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }

    @Override
    public void add(User user, Integer [] roleids) {
        //给密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(user.getPassword());

          user.setPassword(encode);
        userDao.add(user);
        setCheckGroupAndCheckItem(user.getId(),roleids);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public PageResult findPage(String queryString, Integer currentPage, Integer pageSize) {
        // 1：初始化PageHelper
        PageHelper.startPage(currentPage, pageSize);
        // 2：查询
        Page<User> page = userDao.findPage(queryString);
        // 3：封装结果
        return new PageResult(page.getTotal(), page.getResult());

    }

    @Override
    public User findById(Integer id) {


        return userDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {

        return userDao.findCheckItemIdsByCheckGroupId(id);

    }

    @Override
    public void edit(User user, Integer[] roleids) {

    if (user.getPassword()!=null){
        //给密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encode);
    }

        //更新 user
        userDao.edit(user);

        //使用user id 删除中间表
        userDao.deleteT_user_role(user.getId());
        setCheckGroupAndCheckItem(user.getId(),roleids);
    }

    //删除
    @Override
    public void delect(Integer id) {
        userDao.deleteT_user_role(id);
        userDao.delect(id);

    }

    //查询名字
    @Override
    public User findUserName(String username) {
     User  user=   userDao.findUserName(username);
        return user;
    }

    //设置中间表
    private void setCheckGroupAndCheckItem(Integer userid, Integer[] roleids) {
        if(roleids!=null && roleids.length>0){
            for (Integer role : roleids) {
                // 方案一：传递多个参数，使用@Param指定参数的名称
                // checkGroupDao.addCheckGroupAndCheckItem(checkGroupId,checkItemId);
                // 方案二：传递多个参数，使用Map
                Map<String,Object> params = new HashMap<>();
                params.put("userid",userid);
                params.put("roleid",role);
                userDao.addCheckGroupAndCheckItem(params);
            }
        }
    }



}
