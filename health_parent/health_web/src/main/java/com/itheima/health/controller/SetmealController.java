package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:50
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/setmeal")
public class SetmealController {

    // 调用Service
    @Reference
    SetmealService setmealService;

    // 使用Jedis
    @Autowired
    JedisPool jedisPool;

    // 文件上传，将上传的文件传递到七牛云的服务器上
    @RequestMapping(value = "/upload")
    public Result upload(@RequestParam(value = "imgFile") MultipartFile imgFile){
        try {
            // 获取文件名（真实的文件名）（例如：123.jpg）
            String fileName = imgFile.getOriginalFilename();
            // 生成UUID的文件名
            String uuid = UUID.randomUUID().toString();
            // 例如：sdfsdflsdfjl12312sdfds.jpg
            fileName = uuid+fileName.substring(fileName.lastIndexOf("."));

            // 上传七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            // 上传七牛云的代码：向Redis中存放数据，使用集合存储方案，key值为=setmealPicResource；value值为图片名称
            jedisPool.getResource().sadd(RedisMessageConstant.SETMEAL_PIC_RESOURCE,fileName);

            // 返回Result
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    // 保存套餐
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Setmeal setmeal, Integer [] checkgroupIds){
        try {
            // 保存
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    // 分页查询
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.findPage(queryPageBean.getQueryString(),queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        return pageResult;
    }

}
