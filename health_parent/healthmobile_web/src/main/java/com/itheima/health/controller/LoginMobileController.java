package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMobileMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
@RequestMapping(value = "/login")
public class LoginMobileController {

    @Reference
    MemberService memberService;

    @Autowired
    JedisPool jedisPool;

    // 手机快速登录
    @RequestMapping(value = "/check")
    public Result submit(@RequestBody Map map, HttpServletResponse response){
        // 获取用户输入的手机号
        String telephone = (String)map.get("telephone");
        // 获取用户输入的验证码
        String validateCode = (String)map.get("validateCode");
        // 从Redis中获取验证码
        String redisValidateCode = jedisPool.getResource().get(telephone+RedisMobileMessageConstant.SENDTYPE_LOGIN);
        // 校验不通过
        if(redisValidateCode==null || !redisValidateCode.equals(validateCode)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        // 校验通过
        Member member = memberService.findMemberByTelephone(telephone);
        // 如果不是会员，注册会员
        if(member==null){
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        // 保存用户登录状态，使用Cookie存放用户的信息
        Cookie cookie = new Cookie("login_telephone_member",telephone);
        cookie.setPath("/"); // 有效路径
        cookie.setMaxAge(30*24*60*60); // 有效生命时间（秒）  30天
        response.addCookie(cookie);

        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }


}
