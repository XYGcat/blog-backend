package com.xc.blogbackend.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 用户常量
 *
 * @author 星尘
 */
public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * "盐"值：混淆密码
     */
    String SALT = "xc";

    /**
     * Token加密密文，私钥
     */
    String JWT_SECRET_KEY = "Let life be beautiful like summer flowers and death like autumn leaves";

    /**
     * 登录验证拦截器排除特定的请求路径
     */
    List<String> loginExcludedPaths = Arrays.asList("/titleExist", "/blogHomeGetArticleList/**","/blogTimelineGetArticleList/**",
            "/getArticleListByTagId","/getArticleListByCategoryId","/getRecommendArticleById/**","/getArticleListByContent/**",
            "/getHotArticle");

    /**
     * 身份验证拦截器排除特定的请求路径
     */
    List<String> statusExcludedPaths = Arrays.asList("/comment/delete/**");

    /**
     * 登录验证拦截器拦截特定的请求路径
     */
    List<String> loginInterceptPaths = Arrays.asList("/**/add/**","/**/update/**","/**/delete/**","/**/addOrUpdate",
            "/**/updateRole/**","/**/adminUpdateUserInfo","/**/updateTop/**","/**/approve","/**/publishTalk","/**/updateTalk",
            "/**/revert/**","/**/isPublic/**","/**/backDelete/**","/**/deleteTalkById/**","/**/togglePublic/**","/**/toggleTop/**",
            "/**/revertTalk/**","/**/getArticleList","/**/updatePassword","/**/updateOwnUserInfo","/**/getUserList");


    /**
     * 身份验证拦截器拦截特定的请求路径
     */
    List<String> statusInterceptPaths = Arrays.asList("/**/add/**","/**/update/**","/**/delete/**","/**/addOrUpdate",
            "/**/updateRole/**","/**/adminUpdateUserInfo","/**/updateTop/**","/**/approve","/**/publishTalk","/**/updateTalk",
            "/**/revert/**","/**/isPublic/**","/**/backDelete/**","/**/deleteTalkById/**","/**/togglePublic/**","/**/toggleTop/**",
            "/**/revertTalk/**");
}
