package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:50
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/ordersetting")
public class OrderSettingController {

    // 调用Service
    @Reference
    OrderSettingService orderSettingService;

    /**
     *批量导入（保存）
     * @return
     */
    @RequestMapping(value = "/upload")
    public Result upload(MultipartFile excelFile){
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettingsList = new ArrayList<>();
            // 将list转换成orderSettingsList
            for (String[] strings : list) {
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
                orderSettingsList.add(orderSetting);
            }
            // 批量保存
            orderSettingService.addList(orderSettingsList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 初始化当前月对应的预约设置信息
     * @return
     */
    @RequestMapping(value = "/findOrderSettingByOrderDateMonth")
    public Result findOrderSettingByOrderDateMonth(String date){
        try {
            List<Map> list = orderSettingService.findOrderSettingByOrderDateMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据当前时间，更新预约设置的最大人数
     * @return
     */
    @RequestMapping(value = "/updateOrderSettingByOrderDate")
    public Result updateOrderSettingByOrderDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.updateOrderSettingByOrderDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
