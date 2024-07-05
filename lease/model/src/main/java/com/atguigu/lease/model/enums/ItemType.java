package com.atguigu.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemType implements BaseEnum
{

    APARTMENT(1, "公寓"),

    ROOM(2, "房间");

    /**
     * TypeHandler枚举类型转换：Service/Mapper =》 数据库
     */
    @EnumValue
    /**
     * HTTPMessageConverter枚举类型转换：前端 《=》 Controller + @RequestBody/@ResponseBody 请求体JSON字符串
     */
    @JsonValue
    private Integer code;
    private String name;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return name;
    }

    ItemType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

}
