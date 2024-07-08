package com.atguigu.lease.web.app.custom.config;

import com.atguigu.lease.web.app.custom.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-08 16:20:17
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer
{

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(this.authenticationInterceptor)
                .addPathPatterns("/app/**")
                .excludePathPatterns("/app/login/**");
    }
}
