package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:48
 * @Version V1.0
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(String queryString, Integer currentPage, Integer pageSize) {
        // 1：总记录数（SELECT COUNT(*) FROM t_checkitem WHERE CODE = '身高' OR NAME = '身高'）
        // 查询的总记录数封装到total
        // 2：当前页显示数据结果（SELECT * FROM t_checkitem WHERE CODE = '身高' OR NAME = '身高' LIMIT 0,10）
        // limit参数1：从第几条开始检索（(currentPage-1)*pageSize），limit参数2：表示当前页最多显示的记录数（pageSize）
        // 查询结果list封装到rows

        // mybatis的分页插件PageHelper
        // 1：对当前页和当前页显示的记录数进行初始化
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询，返回Page对象，方案一：使用Page
        // Page<CheckItem> page = checkItemDao.findPage(queryString);
        // 3：封装PageResult
        // return new PageResult(page.getTotal(),page.getResult());

        // 2：查询，返回Page对象，方案二：使用PageInfo
        List<CheckItem> list = checkItemDao.findPage(queryString);
        PageInfo<CheckItem> pageInfo = new PageInfo<>(list);
        // 3：封装PageResult
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }


    // 检查项如果在中间表中有关联，不允许删除
    @Override
    public void delete(Integer id) {
        // 删除检查项之前判断，当前检查项在中间表中是否存在记录
        Long count = checkItemDao.findCheckGroupAndCheckItemCountByCheckItemId(id);
        if(count>0){
            throw new RuntimeException("当前检查项在检查组和检查项的中间表中存在数据，不能删除");
        }
        // 删除检查项
        checkItemDao.delete(id);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
