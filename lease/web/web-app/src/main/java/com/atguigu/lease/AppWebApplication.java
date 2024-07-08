package com.atguigu.lease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-08 15:31:11
 */
@SpringBootApplication
// 开启异步功能
@EnableAsync
public class AppWebApplication
{
    public static void main(String[] args) {
        SpringApplication.run(AppWebApplication.class);
    }
}
