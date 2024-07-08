package com.atguigu.lease.web.admin.custom.config;

import com.atguigu.lease.web.admin.custom.converter.StringToBaseEnumConverterFactory;
import com.atguigu.lease.web.admin.custom.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-04 10:20:58
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer
{
    // @Autowired
    // private StringToItemTypeConverter stringToItemTypeConverter;
    //
    // @Override
    // public void addFormatters(FormatterRegistry registry) {
    //     registry.addConverter(this.stringToItemTypeConverter);
    // }

    /* ====================================枚举类型转换==================================== */
    @Autowired
    private StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(this.stringToBaseEnumConverterFactory);
    }

    /* ====================================登录认证拦截==================================== */
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(this.authenticationInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login/**");
    }
}
