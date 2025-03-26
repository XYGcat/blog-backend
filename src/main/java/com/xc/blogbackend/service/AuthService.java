package com.xc.blogbackend.service;

import com.xc.blogbackend.model.domain.vo.TokenVo;
import com.xc.blogbackend.model.domain.vo.UserVo;

/**
 * 用户认证服务
 */
public interface AuthService {

    /**
     * 刷新令牌
     *
     * @param token 令牌
     * @return {@link TokenVo}
     */
    TokenVo refreshAccessToken(String token);

    /**
     * 生成令牌并设置到用户中
     *
     * @param userInfo 用户信息
     * @return {@link TokenVo}
     */
    TokenVo generateTokensAndSetToUser(UserVo userInfo);
}
