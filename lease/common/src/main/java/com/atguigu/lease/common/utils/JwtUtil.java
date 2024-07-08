package com.atguigu.lease.common.utils;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-08 10:56:53
 */
public class JwtUtil
{
    /**
     * 过期时间
     */
    private static long tokenExpiration = 60 * 60 * 1000L;
    /**
     * 令牌签名密钥
     */
    private static SecretKey tokenSignKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    /**
     * 创建令牌
     *
     * @param userId   用户 ID
     * @param username 用户名
     * @return {@link String }
     */
    public static String createToken(Long userId, String username) {
        return Jwts
                // jwt构造器
                .builder()
                // 主题
                .setSubject("USER_INFO")
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                // 负载内容
                .claim("userId", userId)
                .claim("username", username)
                // 使用令牌签名秘钥签名
                .signWith(tokenSignKey)
                // 压缩
                .compact();
    }

    /**
     * 解析令牌
     *
     * @param token 令 牌
     * @return {@link Claims }
     */
    public static Claims parseToken(String token) {
        if (token == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }

        try {
            return Jwts
                    // jwt解析构造器
                    .parserBuilder()
                    // 使用密钥进行解析
                    .setSigningKey(tokenSignKey)
                    // 构建
                    .build()
                    // 从token中解析claim声明
                    .parseClaimsJws(token)
                    // 获取结构体
                    .getBody();
        }
        catch (ExpiredJwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        }
        catch (JwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }

    public static void main(String[] args) {
        // 为方便继续调试其他接口，可以获取一个长期有效的Token，将其配置到Knife4j的全局参数中
        System.out.println(JwtUtil.createToken(1L, "admin"));
    }
}
