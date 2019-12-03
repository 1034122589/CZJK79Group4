package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

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
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

    // 使用Redis
    @Autowired
    JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1：保存套餐信息
        setmealDao.add(setmeal);
        //2：遍历检查组的集合，向套餐和检查组的中间表中插入数据
        if(checkgroupIds!=null && checkgroupIds.length>0){
            // 使用套餐id和检查组id，向套餐和检查组的中间表中插入数据
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
        // 保存套餐（保存数据库）的代码：向Redis中存放数据，使用集合存储方案，key值为=setmealPicDbResource；value值为图片名称
        jedisPool.getResource().sadd(RedisMessageConstant.SETMEAL_PIC_DB_DBRESOURCE,setmeal.getImg());
    }

    @Override
    public PageResult findPage(String queryString, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //  使用resultMap完成集合映射开发
    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    @Override
    public List<Map> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }

    //  不使用resultMap完成集合映射开发（了解）
//    @Autowired
//    CheckGroupDao checkGroupDao;
//
//    @Autowired
//    CheckItemDao checkItemDao;
//
//    @Override
//    public Setmeal findById(Integer id) {
//        Setmeal setmeal = setmealDao.findById(id);
//        List<CheckGroup> list = checkGroupDao.findCheckGroupListBySetmealId(setmeal.getId());
//        // 遍历检查组
//        if(list!=null && list.size()>0){
//            for (CheckGroup checkGroup : list) {
//                List<CheckItem> checkItemList = checkItemDao.findCheckItemsListByCheckGroupId(checkGroup.getId());
//                // 封装到checkGroup中
//                checkGroup.setCheckItems(checkItemList);
//            }
//        }
//        // 封装到setmeal中
//        setmeal.setCheckGroups(list);
//        return setmeal;
//    }

    // 使用套餐id和检查组id，向套餐和检查组的中间表中插入数据
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Object> params = new HashMap<>();
            params.put("setmealId",setmealId);
            params.put("checkgroupId",checkgroupId);
            setmealDao.addSetmealAndCheckGroup(params);
        }

    }
}
