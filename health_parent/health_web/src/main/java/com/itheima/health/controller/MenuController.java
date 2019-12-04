package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    @RequestMapping("/findAll")
    public Result findAll(){
        List<Menu> list = menuService.findAll();
        if (list != null && list.size() > 0){
            return new Result(true,"查询成功",list);
        }
        return new Result(false,"查询失败");
    }
}
