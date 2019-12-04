package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {

    Set<Role> findRolesByUserId(Integer userId);

    void add(Role role);

    void setRoleAndPermission(Map<String, Integer> map);

    void setRoleAndMenu(Map<String, Integer> map);

    Page<Role> findPage(String queryString);

    Role findById(Integer id);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    List<Integer> findMenuIdsByRoleId(Integer id);

    void edit(Role role);

    void deletePermissionIdsByRoleId(Integer id);

    void deleteMenuIdsByRoleId(Integer id);

    void delete(Integer id);

    List<Role> findAll();
}
