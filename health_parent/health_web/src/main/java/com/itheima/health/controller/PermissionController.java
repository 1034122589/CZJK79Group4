package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Reference
    private PermissionService permissionService;

    @RequestMapping("/findAll")
    public Result findAll(){
        List<Permission> list = permissionService.findAll();
        if (list != null && list.size() > 0){
            return new Result(true, MessageConstant.QUERY_SUCCESS,list);
        }
        return new Result(false, MessageConstant.QUERY_FAIL);
    }
}
