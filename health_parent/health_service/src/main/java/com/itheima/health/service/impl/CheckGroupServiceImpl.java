package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        /**
         * 1：传递检查组的基本信息,插入一条数据，到t_checkgroup中
         */
        checkGroupDao.add(checkGroup);
        /**
         2：传递检查项的id（多个）和检查组的id,向检查组和检查项的中间表中存放数据（多个）
         */
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public PageResult findPage(String queryString, Integer currentPage, Integer pageSize) {
        // 1：初始化PageHelper
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询
        Page<CheckGroup> page = checkGroupDao.findPage(queryString);
        // 3：封装结果
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 1：使用检查组id，更新检查组的基本信息，更新t_checkgroup
        checkGroupDao.edit(checkGroup);
        // 2：使用检查组id，删除中间表
        checkGroupDao.deleteCheckGroupAndCheckItemByCheckGroupId(checkGroup.getId());
        // 3：使用检查项id和检查组的id，向中间表中插入数据
        this.setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    // 传递检查项的id（多个）和检查组的id,向检查组和检查项的中间表中存放数据
    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if(checkitemIds!=null && checkitemIds.length>0){
            for (Integer checkItemId : checkitemIds) {
                // 方案一：传递多个参数，使用@Param指定参数的名称
                // checkGroupDao.addCheckGroupAndCheckItem(checkGroupId,checkItemId);
                // 方案二：传递多个参数，使用Map
                Map<String,Object> params = new HashMap<>();
                params.put("checkGroup_Id",checkGroupId);
                params.put("checkItem_Id",checkItemId);
                checkGroupDao.addCheckGroupAndCheckItem(params);
            }
        }
    }
}
