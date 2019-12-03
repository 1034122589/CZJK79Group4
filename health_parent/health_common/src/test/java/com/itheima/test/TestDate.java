package com.itheima.test;

import com.itheima.health.utils.DateUtils;
import org.junit.Test;

/**
 * @ClassName TestDate
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/12/1 16:21
 * @Version V1.0
 */
public class TestDate {

    @Test
    public void test() throws Exception {
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
    }
}
