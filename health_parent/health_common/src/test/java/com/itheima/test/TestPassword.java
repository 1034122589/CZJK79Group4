package com.itheima.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName TestPassword
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/28 17:55
 * @Version V1.0
 */
public class TestPassword {

    /**
     * spring security中的BCryptPasswordEncoder方法采用SHA-256 +随机盐+密钥对密码进行加密。SHA系列是Hash算法，不是加密算法，使用加密算法意味着可以解密（这个与编码/解码一样），但是采用Hash处理，其过程是不可逆的。

     （1）加密(encode)：注册用户时，使用SHA-256+随机盐+密钥把用户输入的密码进行hash处理，得到密码的hash值，然后将其存入数据库中。

     （2）密码匹配(matches)：用户登录时，密码匹配阶段并没有进行密码解密（因为密码经过Hash处理，是不可逆的），而是使用相同的算法把用户输入的密码进行hash处理，得到密码的hash值，然后将其与从数据库中查询到的密码hash值进行比较。如果两者相同，说明用户输入的密码正确。

     */
    @Test
    public void testPassword(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 存到数据库
        String password1 = passwordEncoder.encode("123"); // $2a$10$rGznCTHHFQ8zqw/THEKK/.QCgKHKOv82yxtxgmRZeA2qRShjnQZ3y
        String password2 = passwordEncoder.encode("123"); // $2a$10$F2AK8WatBrImbeIgITUuJepNt4Y6v4ZTGguYyOp8Dwf.iwOXUebly
        System.out.println(password1);
        System.out.println(password2);

        // 登录，匹配的时候（前面的值：表示登录页面输入的123，后面的值表示从数据库查询获取的值）
        boolean flag = passwordEncoder.matches("123","$2a$10$rGznCTHHFQ8zqw/THEKK/.QCgKHKOv82yxtxgmRZeA2qRShjnQZ3y");
        System.out.println(flag);
    }
}
