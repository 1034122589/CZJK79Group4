package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
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
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    // 会员
    @Autowired
    MemberDao memberDao;

    // 预约订单（套餐占比）
    @Autowired
    OrderDao orderDao;

    @Override
    public Map<String, Object> findBusinessReportData() throws Exception {
        // 日期工具类
        // 当前时间
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        // 根据当前时间，计算本周的周一
        String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 根据当前时间，计算本周的周日
        String sunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        // 根据当前时间，计算本月的1号
        String firstOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        // 根据当前时间，计算本月的最后1号
        String lastOfMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

        // 会员相关数据
        // todayNewMember（今日新增会员数）
        Integer todayNewMember = memberDao.findTodayNewMember(today);
        // totalMember（总会员数）
        Integer totalMember = memberDao.findTotalMember();
        // thisWeekNewMember（本周新增会员数）
        Integer thisWeekNewMember = memberDao.findThisNewMember(monday);
        // thisWeekNewMember（本月新增会员数）
        Integer thisMonthNewMember = memberDao.findThisNewMember(firstOfMonth);

        // 预约订单相关数据
        // todayOrderNumber（今日预约数）
        Integer todayOrderNumber = orderDao.findTodayOrderNumber(today);
        // todayVisitsNumber（今日到诊数）
        Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(today);
        // thisWeekOrderNumber（本周预约数）
        Map map1 = new HashMap();
        map1.put("begin",monday);
        map1.put("end",sunday);
        Integer thisWeekOrderNumber = orderDao.findThisOrderNumber(map1);
        // thisWeekVisitsNumber（本周到诊数）
        Map map3 = new HashMap();
        map3.put("begin",monday);
        map3.put("end",sunday);
        Integer thisWeekVisitsNumber = orderDao.findThisVisitsNumber(map3);
        // thisMonthOrderNumber（本月预约数）
        Map map2 = new HashMap();
        map2.put("begin",firstOfMonth);
        map2.put("end",lastOfMonth);
        Integer thisMonthOrderNumber = orderDao.findThisOrderNumber(map2);
        // thisMonthVisitsNumber（本月到诊数）
        Map map4 = new HashMap();
        map4.put("begin",firstOfMonth);
        map4.put("end",lastOfMonth);
        Integer thisMonthVisitsNumber = orderDao.findThisVisitsNumber(map4);
        // 热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        // 响应数据List<Map<String, Object>>
        Map<String, Object> map = new HashMap<>();
        map.put("reportDate",today); // 报表产生的日期，当前时间
        map.put("todayNewMember",todayNewMember);
        map.put("totalMember",totalMember);
        map.put("thisWeekNewMember",thisWeekNewMember);
        map.put("thisMonthNewMember",thisMonthNewMember);
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("todayVisitsNumber",todayVisitsNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }
}
