package com.itheima.health.security;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/28 17:10
 * @Version V1.0
 */
// 1：定义UserService类，实现UserDetailsService接口
@Component
public class SpringSecurityUserService implements UserDetailsService{

    @Reference
    UserService userService;


    // 只要在登录页面执行/login.do的url，就会执行UserService类中的loadUserByUsername方法
    // 传递username传递的参数（登录名）
    // 需要完成：<security:user name="admin" password="{noop}admin" authorities="ROLE_ADMIN"></security:user>
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 完成认证
        // 使用用户名查询，对应的用户对象
        com.itheima.health.pojo.User user = userService.findUserByUsername(username);
        // return null，用户名输入有误，抛出异常(org.springframework.security.authentication.InternalAuthenticationServiceException: UserDetailsService returned null)，并跳转到authentication-failure-url="/login.html"
        if(user==null){
            return null;
        }
        // 完成授权，对list授权
        List<GrantedAuthority> list = new ArrayList<>();
        // 从数据库查询
        if(user!=null){
            Set<Role> roles = user.getRoles();
            // 获取用户下的所有角色
            if(roles!=null && roles.size()>0){
                for (Role role : roles) {
                    //list.add(new SimpleGrantedAuthority(role.getKeyword()));// 基于角色完成权限控制
                    // 获取用户下的所有权限
                    Set<Permission> permissions = role.getPermissions();
                    if(permissions!=null && permissions.size()>0){
                        for (Permission permission : permissions) {
                            list.add(new SimpleGrantedAuthority(permission.getKeyword()));// 基于权限完成权限控制
                        }
                    }
                }
            }
        }


        // 使用BCryptPasswordEncoder加密
        String password = user.getPassword();
        UserDetails userDetails = new User(username,password,list);
        return userDetails;
    }
}
