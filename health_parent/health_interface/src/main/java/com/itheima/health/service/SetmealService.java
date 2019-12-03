package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(String queryString, Integer currentPage, Integer pageSize);

    String findAll();

    String findById(Integer id);

    List<Map> getSetmealReport();
}
