package com.atguigu.lease.web.admin.custom.converter;

import com.atguigu.lease.model.enums.ItemType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-04 10:17:23
 */
@Component
public class StringToItemTypeConverter implements Converter<String, ItemType>
{
    @Override
    public ItemType convert(String code) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType
                    .getCode()
                    .equals(Integer.valueOf(code))) {
                return itemType;
            }
        }
        throw new IllegalArgumentException("code非法");
    }
}
