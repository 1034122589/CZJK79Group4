package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;

import java.util.List;

public interface UserService {

    User findUserByUsername(String username);


    void add(User user, Integer [] roleids);

    List<Role> findAll();

    PageResult findPage(String queryString, Integer currentPage, Integer pageSize);

    User findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(User user, Integer[] roleids);

    void delect(Integer id);

    User findUserName(String username);
}
