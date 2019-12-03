package com.itheima.test;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @ClassName TestCalader
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/30 18:17
 * @Version V1.0
 */
public class TestCalader {

    @Test
    public void test(){
        List<String> months = new ArrayList<>();
        // 根据当前时间，获取过去12个月年月情况
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);// -12表示根据当前时间，将Calendar对象向前推12个月（从2018-12）
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);// 从2018-12，累加计算年月
            months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        System.out.println(months);
    }
}
