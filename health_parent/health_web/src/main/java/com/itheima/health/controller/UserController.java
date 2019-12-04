package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:50
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
     @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Reference
    UserService userService;

    // 从SpringSecurity中获取用户信息
    @RequestMapping(value = "/getUsername")
    public Result getUsername(){
        try {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 使用登录名，查询当前登录名对应用户信息
            //com.itheima.health.pojo.User user2 = userService.findUserByUsername(user.getUsername());

            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
    // 添加user
    @RequestMapping(value = "/add")
    public Result add(@RequestBody com.itheima.health.pojo.User user, Integer [] roleids){

        try {
            userService.add(user,roleids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }

        return new Result(true, MessageConstant.ADD_USER_SUCCESS);
    }


    // 角色查询
    @RequestMapping(value = "/findAll")
    public Result findAll(){

        try {
       List<Role>  list =userService.findAll();
            return new Result(true,"角色查询成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"角色查询失败");
        }


    }

    // 分页查询
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = userService.findPage(queryPageBean.getQueryString(),queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        return pageResult;
    }

    // 用户的主键查询
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        // ID查询
        com.itheima.health.pojo.User user = userService.findById(id);
        if(user!=null){
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,user);
        }
        else{
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


    // 使用用户id，查询用户个=和角色的中间表，获取角色的集合，返回List<Integer>
    @RequestMapping(value = "/findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id){
        // 使用检查组的id，查询检查项的ID集合
        List<Integer> list = userService.findCheckItemIdsByCheckGroupId(id);
        return list;
    }
    // 编辑检查组
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody com.itheima.health.pojo.User user, Integer [] roleids){
        try {

            // 编辑
            userService.edit(user,roleids);
            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USERFAIL);
        }
    }

    // 编辑检查组
    @RequestMapping(value = "/delect")
    @PreAuthorize(value = "hasAuthority('ROLE_DELETE')")
    public Result delect(Integer id){
        try {

            // 编辑
            userService.delect(id);
            return new Result(true, "删除用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "权限不足");
        }
    }

    // 使用用户id，查询用户个=和角色的中间表，获取角色的集合，返回List<Integer>
    @RequestMapping(value = "/findname")
    public Result findUserName(String username){
        // 使用检查组的id，查询检查项的ID集合
        com.itheima.health.pojo.User user = userService.findUserName(username);
        if (user==null){
            return new Result(true, "用户名可用使用");
        }else {
            return new Result(false, "用户已存在");
        }

    }

}
