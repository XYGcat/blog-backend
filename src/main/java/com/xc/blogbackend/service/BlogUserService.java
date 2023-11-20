package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogUser;

import javax.servlet.http.HttpServletRequest;

/**
* @author XC
* @description 针对表【blog_user】的数据库操作Service
* @createDate 2023-11-10 18:27:39
*/
public interface BlogUserService extends IService<BlogUser> {

    /**
     * 用户登录
     *
     * @param username  用户账号
     * @param password 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    BlogUser userLogin(String username, String password,String ip, HttpServletRequest request);

    /**
     * 用户注册
     *
     * @param username   用户账户
     * @param password  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String username,String password,String checkPassword);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    BlogUser getSafetyUser(BlogUser originUser);

    /**
     * 根据id查询用户信息
     *
     * @param user_id 用户id
     * @return
     */
    BlogUser getOneUserInfo(Integer user_id);
}
