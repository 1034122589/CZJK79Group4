package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberBirthdayDao;
import com.itheima.health.service.MemberBirthdayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:48
 * @Version V1.0
 */
@Service(interfaceClass = MemberBirthdayService.class)
@Transactional
public class MemberBirthdayServiceImpl implements MemberBirthdayService {

    @Autowired
    MemberBirthdayDao memberBirthdayDao;

    @Override
    public List<Integer> getMemberBirthday() {
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int f= 0;
        List<Integer> list1 = new ArrayList<>();
        try {
            List<String> list = memberBirthdayDao.getMemberBirthday();
            for (String date : list) {
                if (date!= null && date.length()> 0) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = simpleDateFormat.parse(date);
                    long oldTime = date1.getTime();
                    long nowTime = System.currentTimeMillis();
                    long age = (nowTime-oldTime)/3600/1000/24/365;
                    if (age <= 18) {
                        a++;
                    }else if (age > 18 && age <= 30) {
                        b++;
                    }else if (age > 30 && age <= 45) {
                        c++;
                    }else {
                        d++;
                    }
                }else {
                    f++;
                }
            }
            list1.add(a);
            list1.add(b);
            list1.add(c);
            list1.add(d);
            list1.add(f);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list1;
    }
}
