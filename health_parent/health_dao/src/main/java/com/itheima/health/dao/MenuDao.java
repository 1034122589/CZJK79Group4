package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;

import java.util.List;
import java.util.Set;

public interface MenuDao {

    Set<Menu> findMenusByRoleId(Integer roleId);

    List<Menu> findAll();
}
