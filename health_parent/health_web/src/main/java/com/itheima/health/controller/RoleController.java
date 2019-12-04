package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    /**
     * 新增
     *
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize(value = "hasAuthority('ROLE_ADD')")
    public Result add(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        try {
            roleService.add(role, permissionIds, menuIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
        return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
    }

    /**
     * 角色分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    @PreAuthorize(value = "hasAuthority('ROLE_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = roleService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Role role = roleService.findById(id);
        if (role != null) {
            return new Result(true, MessageConstant.QUERY_SUCCESS, role);
        }
        return new Result(false, MessageConstant.QUERY_FAIL);
    }

    /**
     * 根据角色id查询对应的所有权限id
     *
     * @param id
     * @return
     */
    @RequestMapping("/findPermissionIdsByRoleId")
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        List<Integer> list = roleService.findPermissionIdsByRoleId(id);
        return list;
    }

    /**
     * 根据角色id查询对应的所有菜单id
     *
     * @param id
     * @return
     */
    @RequestMapping("/findMenuIdsByRoleId")
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        List<Integer> list = roleService.findMenuIdsByRoleId(id);
        return list;
    }

    /**
     * 编辑
     *
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @RequestMapping("/edit")
    @PreAuthorize(value = "hasAuthority('ROLE_EDIT')")
    public Result edit(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        try {
            roleService.edit(role, permissionIds, menuIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SUCCESS);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @PreAuthorize(value = "hasAuthority('ROLE_DELETE')")
    public Result delete(Integer id) {
        try {
            roleService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_SUCCESS);
    }


    @RequestMapping("/findAll")
    public Result findAll(){
        //查询所有的检查组
        List<Role> list = roleService.findAll();
        if(list != null && list.size()>0){
            return  new Result(true, MessageConstant.QUERY_SUCCESS,list);
        }
        return new Result(false, MessageConstant.QUERY_FAIL);
    }
}
