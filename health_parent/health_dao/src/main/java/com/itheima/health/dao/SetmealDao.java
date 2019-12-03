package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    void add(Setmeal setmeal);

    void addSetmealAndCheckGroup(Map<String, Object> params);

    Page<Setmeal> findPage(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map> getSetmealReport();
}
