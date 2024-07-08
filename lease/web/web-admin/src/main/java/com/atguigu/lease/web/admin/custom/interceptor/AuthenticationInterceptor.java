package com.atguigu.lease.web.admin.custom.interceptor;

import com.atguigu.lease.common.login.LoginUser;
import com.atguigu.lease.common.login.LoginUserHolder;
import com.atguigu.lease.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-08 11:13:51
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor
{
    /**
     * 预处理
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return boolean
     * @throws Exception 例外
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 我们约定，前端登录后，后续请求都将JWT，放置于HTTP请求的Header中，其Header的key为`access-token`
        final String token = request.getHeader("access-token");
        final Claims claims = JwtUtil.parseToken(token);
        final Long userId = claims.get("userId", Long.class);
        final String userName = claims.get("username", String.class);
        LoginUserHolder.setLoginUser(LoginUser
                .builder()
                .userId(userId)
                .username(userName)
                .build());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserHolder.clear();
    }
}
