package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName SetmealMobileController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/25 15:40
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/setmeal")
public class SetmealMobileController {

    @Reference
    SetmealService setmealService;

    // 查询所有的套餐
    @RequestMapping(value = "/getSetmeal")
    public Result getSetmeal(){
        String setmealList = setmealService.findAll();

        if(setmealList!=null && setmealList.length()>0){
            JSONArray s = JSON.parseArray(setmealList);
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,s);
        }
        else{
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    // 使用套餐id，查询套餐id所对应的套餐信息（详情）
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        String setmeal = setmealService.findById(id);
        if(setmeal!=null){
            JSONObject object = JSON.parseObject(setmeal);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,object);
        }
        else{
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

}
