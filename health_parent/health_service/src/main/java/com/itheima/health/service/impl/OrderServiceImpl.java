package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    // 预约订单
    @Autowired
    OrderDao orderDao;

    // 预约设置
    @Autowired
    OrderSettingDao orderSettingDao;

    // 会员
    @Autowired
    MemberDao memberDao;

    @Override
    public Result submitOrder(Map map) throws Exception {

        try {
            //1：获取orderdate预约时间
            String orderDate = (String)map.get("orderDate");
            // 将orderDate从字符串转换成日期类型
            Date date = DateUtils.parseString2Date(orderDate);
            //2：使用预约时间判断预约设置表是否存在该时间
            OrderSetting orderSetting = orderSettingDao.findOrderSettingByOrderDate(date);
            // * 如果不存在该时间：提示“该时间不能进行预约”
            if(orderSetting==null){
                return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            //3：获取预约设置表number（最多可预约人数）和reservations（已经预约人数）
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            //* 如果reservations>=number，提示“预约已满”
            if(reservations>=number){
                return new Result(false,MessageConstant.ORDER_FULL);
            }

            // 4：获取telephone手机号
            String telephone = (String)map.get("telephone");
            // 5：使用手机号，查询会员，判断当前手机号是否已经注册会员
            Member member = memberDao.findMemberByTelephone(telephone);
            //6：如果不是会员，需要自动注册会员，同时返回会员ID
            if(member==null){
                member = new Member();
                member.setPhoneNumber((String)map.get("telephone"));
                member.setIdCard((String)map.get("idCard"));
                member.setName((String)map.get("name"));
                member.setSex((String)map.get("sex"));
                member.setRegTime(new Date()); // 会员的注册时间
                memberDao.add(member);
            }
            //7：如果是会员，判断当前会员id、当前预约时间、当前套餐id，是否在预约订单表存在数据
            else{
                // 组织查询条件
                Order order = new Order(member.getId(),date,null,null,Integer.parseInt((String)map.get("setmealId")));
                List<Order> list = orderDao.findOrderListByCondition(order);
                //* 如果存在，提示：“当前会员已经在该时间段已经预约该套餐，不能重复预约”
                if(list!=null && list.size()>0){
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }

            //8：保存预约订单，向t_order表保存数据
            Order order = new Order(member.getId(),date,(String)map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String)map.get("setmealId")));
            orderDao.add(order); // 返回订单id
            //9：更新预约设置表，根据预约时间，将reservations（实际预约人数）+1
            orderSettingDao.updateReservationsByOrderDate(date);
            return new Result(true,MessageConstant.ORDER_SUCCESS,order);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抛出异常");
            //return new Result(false, MessageConstant.ORDER_ERROR);
        }

    }

    @Override
    public Map findById(Integer id) {
        Map map = orderDao.findById(id);
        if(map!=null){
            Date date = (Date)map.get("orderDate");
            // 将日期转化成字符串，格式yyyy-MM-dd
            try {
                String sDate = DateUtils.parseDate2String(date);
                map.put("orderDate",sDate);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("运行时异常");
            }
        }
        return map;
    }
}
