package com.atguigu.lease.common.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-08 15:55:17
 */
@Data
// 配置文件
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSMSProperties
{

    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint;
}
