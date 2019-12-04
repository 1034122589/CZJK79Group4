package com.itheima.health.dao;

import com.itheima.health.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(Integer roleId);

    List<Permission> findAll();
}
