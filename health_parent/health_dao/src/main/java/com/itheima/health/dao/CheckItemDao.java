package com.itheima.health.dao;

import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    // Page<CheckItem> findPage(String queryString);
    List<CheckItem> findPage(String queryString);

    void delete(Integer id);

    Long findCheckGroupAndCheckItemCountByCheckItemId(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemsListByCheckGroupId(Integer checkGroupId);
}
