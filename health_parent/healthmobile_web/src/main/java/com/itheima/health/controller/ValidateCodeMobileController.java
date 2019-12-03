package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMobileMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName SetmealMobileController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/25 15:40
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/validateCode")
public class ValidateCodeMobileController {

    @Autowired
    JedisPool jedisPool;

    // 发送验证码（场景：体检预约）
    @RequestMapping(value = "/send4Order")
    public Result send4Order(String telephone){
        // 1：传递手机号
        // 2：生成4位验证码，使用手机号，发送短信（SMSUtils.java）
        Integer code4 = ValidateCodeUtils.generateValidateCode(4);
        try {
            //SMSUtils.sendShortMessage(telephone,code4.toString());
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送短信成功！验证码是："+code4);
        /**
         3：将手机号和生成的验证码存放到Redis中（5分钟后过期）
         key：                                     value
        手机号+001                                 验证码
         */
        jedisPool.getResource().setex(telephone+ RedisMobileMessageConstant.SENDTYPE_ORDER,5*60,code4.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    // 发送验证码（场景：登录）
    @RequestMapping(value = "/send4Login")
    public Result send4Login(String telephone){
        // 1：传递手机号
        // 2：生成4位验证码，使用手机号，发送短信（SMSUtils.java）
        Integer code4 = ValidateCodeUtils.generateValidateCode(4);
        try {
            //SMSUtils.sendShortMessage(telephone,code4.toString());
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送短信成功！验证码是："+code4);
        /**
         3：将手机号和生成的验证码存放到Redis中（5分钟后过期）
         key：                                     value
         手机号+001                                 验证码
         */
        jedisPool.getResource().setex(telephone+ RedisMobileMessageConstant.SENDTYPE_LOGIN,5*60,code4.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
