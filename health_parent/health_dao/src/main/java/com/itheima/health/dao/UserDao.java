package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User findUserByUsername(String username);


    void add(User user);

    Page<User> findPage(String queryString);

    User findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(User user);

    void deleteT_user_role(Integer id);

    void addCheckGroupAndCheckItem(Map<String, Object> params);

    void delect(Integer id);
}
