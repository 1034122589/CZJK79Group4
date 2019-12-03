package com.itheima.test;

import com.itheima.health.utils.ValidateCodeUtils;
import org.junit.Test;

/**
 * @ClassName TestValidateCode
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/25 15:09
 * @Version V1.0
 */
public class TestValidateCode {

    // 生成验证码
    @Test
    public void testValidateCode(){
        // 4位
        Integer code4 = ValidateCodeUtils.generateValidateCode(4);
        // 6位
        Integer code6 = ValidateCodeUtils.generateValidateCode(6);

        System.out.println(code4);
        System.out.println(code6);
    }

}
