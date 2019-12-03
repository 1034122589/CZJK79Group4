package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:48
 * @Version V1.0
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void addList(List<OrderSetting> orderSettingsList) {
        if(orderSettingsList!=null && orderSettingsList.size()>0){
            for (OrderSetting orderSetting : orderSettingsList) {
                // 1：使用预约时间，查询当前预约时间在数据库中是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                // 2：如果存在：使用预约时间去更新最多预约人数
                if(count>0){
                    orderSettingDao.updateNumberByOrderDate(orderSetting);
                }
                // 3：如果不存在：新增
                else{
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    // 参数date：2019-11
    @Override
    public List<Map> findOrderSettingByOrderDateMonth(String date) {
        String beginDate = date+"-01";
        String endDate = date+"-31";
        Map<String,Object> params = new HashMap<>();
        params.put("begin",beginDate);
        params.put("end",endDate);
        // 范围查询
        List<OrderSetting> list = orderSettingDao.findOrderSettingByOrderDateBetween(params);
        // 组织返回的数据
        List<Map> mapList = new ArrayList<>();
        if(list!=null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                Map map = new HashMap();
                map.put("date",orderSetting.getOrderDate().getDate()); // 获取日
                map.put("number",orderSetting.getNumber());  // 最多可预约人数
                map.put("reservations",orderSetting.getReservations());  // 已经预约的人数
                mapList.add(map);
            }
        }
        return mapList;
    }

    @Override
    public void updateOrderSettingByOrderDate(OrderSetting orderSetting) {
        // 1：使用预约时间，查询当前预约时间在数据库中是否存在
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        // 2：如果存在：使用预约时间去更新最多预约人数
        if(count>0){
            orderSettingDao.updateNumberByOrderDate(orderSetting);
        }
        // 3：如果不存在：新增
        else{
            orderSettingDao.add(orderSetting);
        }
    }
}
