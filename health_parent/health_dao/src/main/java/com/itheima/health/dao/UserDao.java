package com.itheima.health.dao;

import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {

    User findUserByUsername(String username);


}
