package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Override
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //新增role
        roleDao.add(role);
        //根据roleId插入中间表数据 添加关联关系
        setRoleAndPermission(role.getId(),permissionIds);
        setRoleAndMenu(role.getId(),menuIds);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Role> page = roleDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询role 实现数据回显
     * @param id
     * @return
     */
    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    /**
     * 根据角色id查询对应的所有权限id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        return roleDao.findPermissionIdsByRoleId(id);
    }

    /**
     * 根据角色id查询对应的所有菜单id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        return roleDao.findMenuIdsByRoleId(id);
    }

    /**
     * 编辑更改角色内容
     * @param role
     * @param permissionIds
     * @param menuIds
     */
    @Override
    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleDao.edit(role);
        //删除当前关联关系
        roleDao.deletePermissionIdsByRoleId(role.getId());
        roleDao.deleteMenuIdsByRoleId(role.getId());
        //新建关联关系
        //根据roleId插入中间表数据 添加关联关系
        setRoleAndPermission(role.getId(),permissionIds);
        setRoleAndMenu(role.getId(),menuIds);
    }

    /**
     * 删除角色
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //删除关联关系
        roleDao.deletePermissionIdsByRoleId(id);
        roleDao.deleteMenuIdsByRoleId(id);
        //删除role
        roleDao.delete(id);
    }

    /**
     * 查询所有角色 实现数据回显
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    private void setRoleAndMenu(Integer roleId, Integer[] menuIds) {
        if(menuIds != null && menuIds.length >0){
            for (Integer menuId : menuIds) {
                Map<String,Integer> map = new HashMap<String, Integer>();
                map.put("role_id",roleId);
                map.put("menu_id",menuId);
                roleDao.setRoleAndMenu(map);
            }
        }
    }

    private void setRoleAndPermission(Integer roleId, Integer[] permissionIds) {
        if(permissionIds != null && permissionIds.length >0){
            for (Integer permissionId : permissionIds) {
                Map<String,Integer> map = new HashMap<String, Integer>();
                map.put("role_id",roleId);
                map.put("permission_id",permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }
}
