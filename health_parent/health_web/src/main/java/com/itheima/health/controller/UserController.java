package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.jute.compiler.JString;
import org.springframework.beans.factory.annotation.Autowired;
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
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.ImageProducer;
import java.util.*;

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

    @Autowired
    JedisPool jedisPool;

    // 从SpringSecurity中获取用户信息,和用户菜单
    @RequestMapping(value = "/getUsername")
    public Result getUsername() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 使用登录名，获取用户菜单
            String userName = user.getUsername();

            List menu = getMenu(userName);

            Map<String,Object> map = new HashMap<>();

            map.put("username",userName);
            map.put("menuList",menu);

            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    //获取用户菜单动态展示
    private List getMenu(String userName) {
        // 使用登录名，查询当前登录名对应用户信息
        com.itheima.health.pojo.User user2 = userService.findUserByUsername(userName);
        //封装当前用户菜单信息
        List<Map<String, Object>> menuList = new ArrayList<>();
        //1.封装工作台(固定)
        Map<String, Object> work = new HashMap<>();
        work.put("path", "1");
        work.put("title", "工作台");
        work.put("icon", "fa-dashboard");
        menuList.add(work);
        //2.封装当前用户动态菜单
        //2.1获取当前用户所有菜单信息
        Set<Role> roles = user2.getRoles();
        LinkedHashSet<Menu> menus = null;
        if (roles == null) {
//            //普通用户
//            Map<String, Object> common = new HashMap<>();
//            common.put("path", "2");
//            common.put("title", "会员管理");
//            common.put("icon", "fa-user-md");
//            common.put("children", "fa-user-md");
//
//            List<Map> commonCh = new ArrayList<>();
//            menuList.add(work);
            return null;
        }
        for (Role role : roles) {
            if (role == null) {
                return null;
            }
            menus = role.getMenus();
        }
        //2.2封装菜单信息
        if (menus == null) {
            return null;
        }
        //封装一级菜单
        Map<String, Object> parent = null;
        for (Menu menu : menus) {
            if (menu.getParentMenuId() == null) {
                parent = new HashMap<>();
                parent.put("path", menu.getPath());
                parent.put("title", menu.getName());
                parent.put("icon", menu.getIcon());
                //封装二级菜单
                List<Map<String, Object>> children = new ArrayList<>();
                for (Menu menu1 : menus) {
                    if (menu1.getParentMenuId() == menu.getId()) {
                        Map<String, Object> childrenMap = new HashMap<>();
                        childrenMap.put("path", menu1.getPath());
                        childrenMap.put("title", menu1.getName());
                        childrenMap.put("linkUrl", menu1.getLinkUrl());
                        children.add(childrenMap);
                    }
                }
                parent.put("children",children);
                menuList.add(parent);
            }
        }
        return menuList;
    }
}
