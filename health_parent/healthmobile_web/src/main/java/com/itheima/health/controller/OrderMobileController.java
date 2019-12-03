package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMobileMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @ClassName SetmealMobileController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/25 15:40
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/order")
public class OrderMobileController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    // 体检预约（保存预约订单）
    @RequestMapping(value = "/submit")
    public Result submit(@RequestBody Map map){

        //1：获取页面的手机号和输入的验证码
        String telephone = (String)map.get("telephone");
        String validateCode = (String)map.get("validateCode");
        //2：使用手机号从Redis中获取Redis里的验证码
        String redisCode = jedisPool.getResource().get(telephone + RedisMobileMessageConstant.SENDTYPE_ORDER);
        //3：使用输入的验证码和Redis里的验证码比对,没有匹配上：提示“验证码输入有误”
        if(redisCode==null || !redisCode.equals(validateCode)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        //如果匹配成功，继续执行
        // 4：组织orderInfo中的数据，传递给OrderServiceImpl处理
        map.put("orderType", Order.ORDERTYPE_WEIXIN); // 从微信端进行的预约
        // 返回值定义Result
        Result result = null;
        try {
            result = orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_ERROR); // false
        }
        return result; // true/false

    }

    // 使用订单ID，查询订单的详情
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map); // true
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL); // false
        }

    }

}
