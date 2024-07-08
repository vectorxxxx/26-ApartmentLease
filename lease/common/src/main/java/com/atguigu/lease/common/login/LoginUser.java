package com.atguigu.lease.common.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-07-08 11:36:38
 */
@Data
@AllArgsConstructor
@Builder
public class LoginUser
{

    private Long userId;
    private String username;
}
