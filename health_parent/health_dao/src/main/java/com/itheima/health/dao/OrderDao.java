package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findOrderListByCondition(Order order);

    void add(Order order);

    Map findById(Integer id);

    Integer findTodayOrderNumber(String today);

    Integer findTodayVisitsNumber(String today);

    Integer findThisOrderNumber(Map map);

    Integer findThisVisitsNumber(Map map);

    List<Map> findHotSetmeal();
}
