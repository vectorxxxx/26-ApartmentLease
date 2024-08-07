package com.atguigu.lease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-03 16:17:47
 */
@SpringBootApplication
@EnableScheduling
public class AdminWebApplication
{
    public static void main(String[] args) {
        SpringApplication.run(AdminWebApplication.class, args);
    }
}
