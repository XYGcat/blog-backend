package com.xc.blogbackend.contant;

/**
 * 用户常量
 *
 * @author 星尘
 */
public interface BlogUserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    // ---- 权限 ----
    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

    /**
     * 超级管理员密码
     */
    String ADMIN_PASSWORD = "318807xc";

    /**
     * "盐"值：混淆密码
     */
    String SALT = "xc";
}
