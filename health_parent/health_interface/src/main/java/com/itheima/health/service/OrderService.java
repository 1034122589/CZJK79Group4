package com.itheima.health.service;

import com.itheima.health.entity.Result;

import java.util.Map;

public interface OrderService {

    Result submitOrder(Map map) throws Exception;

    Map findById(Integer id);
}
