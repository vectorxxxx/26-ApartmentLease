package com.atguigu.lease.web.admin.custom.converter;

import com.atguigu.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * @author VectorX
 * @version V1.0
 * @description WebDataBinder枚举类型转换：前端 =》 Controller + @RequestParam 请求参数
 * @date 2024-07-04 10:27:39
 */
@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum>
{
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return code -> {
            for (T enumConstant : targetType.getEnumConstants()) {
                if (enumConstant
                        .getCode()
                        .equals(Integer.valueOf(code))) {
                    return enumConstant;
                }
            }
            throw new IllegalArgumentException("code非法");
        };
    }
}
