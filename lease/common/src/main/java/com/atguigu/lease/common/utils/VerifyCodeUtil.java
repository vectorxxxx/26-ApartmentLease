package com.atguigu.lease.common.utils;

import java.util.Random;

/**
 * @author VectorX
 * @version V1.0
 * @description 验证码工具类
 * @date 2024-07-08 16:06:43
 */
public class VerifyCodeUtil
{
    public static String getVerifyCode(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
