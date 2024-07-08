package com.atguigu.lease.common.login;

/**
 * @author VectorX
 * @version V1.0
 * @description 登录用户持有者工具类
 * @date 2024-07-08 11:34:58
 */
public class LoginUserHolder
{
    /**
     * ThreadLocal声明为静态全局变量
     * <p>
     * 注意：每个线程单独的副本，互不影响
     */
    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    /**
     * 设置登录用户
     *
     * @param loginUser 登录用户
     */
    public static void setLoginUser(LoginUser loginUser) {
        threadLocal.set(loginUser);
    }

    /**
     * 获取登录用户
     *
     * @return {@link LoginUser }
     */
    public static LoginUser getLoginUser() {
        return threadLocal.get();
    }

    /**
     * 清除登录用户
     */
    public static void clear() {
        threadLocal.remove();
    }
}
