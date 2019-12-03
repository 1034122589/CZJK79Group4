package com.itheima.health.job;

import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName ClearImgJobDemo
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/22 18:16
 * @Version V1.0
 */
public class ClearImgJobDemo {

    @Autowired
    JedisPool jedisPool;

    // 删除图片（删除七牛云、删除Redis）
    public void clearImg(){
        // 使用Redis的集合存储特点，比较集合中的key获取不同的value值
        Set<String> set = jedisPool.getResource().sdiff(RedisMessageConstant.SETMEAL_PIC_RESOURCE, RedisMessageConstant.SETMEAL_PIC_DB_DBRESOURCE);
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            String img = iterator.next();
            System.out.println("要删除的图片："+img);
            //1：删除七牛云上垃圾图片
            QiniuUtils.deleteFileFromQiniu(img);
            //2：删除Redis中key值SetmealPicResource的垃圾图片
            jedisPool.getResource().srem(RedisMessageConstant.SETMEAL_PIC_RESOURCE,img);
        }



    }
}
