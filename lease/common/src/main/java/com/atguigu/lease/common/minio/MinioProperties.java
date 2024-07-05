package com.atguigu.lease.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-05 14:00:54
 */
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties
{
    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;
}
