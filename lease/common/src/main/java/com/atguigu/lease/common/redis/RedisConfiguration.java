package com.atguigu.lease.common.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-08 17:49:35
 */
@Configuration
public class RedisConfiguration
{

    /**
     * String 自定义RedisTemplate
     *
     * @param redisConnectionFactory Redis 连接工厂
     * @return {@link RedisTemplate }<{@link String }, {@link Object }>
     */
    @Bean
    public RedisTemplate<String, Object> stringObjectRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.java());
        return template;
    }
}
