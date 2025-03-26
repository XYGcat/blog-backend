package com.xc.blogbackend.service.impl;

import com.xc.blogbackend.enums.ErrorCode;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.vo.TokenVo;
import com.xc.blogbackend.model.domain.vo.UserVo;
import com.xc.blogbackend.service.AuthService;
import com.xc.blogbackend.service.BlogUserService;
import com.xc.blogbackend.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisTemplate redisTemplate;
    private final BlogUserService userService;

    @Override
    public TokenVo refreshAccessToken(String token) {
        Claims claims = JwtUtils.parseToken(token);
        Boolean tokenExpired = JwtUtils.isTokenExpired(claims);
        if (tokenExpired){
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRE, "刷新token已过期");
        }

        String userId = claims.getSubject();
        Object redisRefreshToken = redisTemplate.opsForValue().get("REFRESH_TOKEN_" + userId);
        if (ObjectUtils.isEmpty(redisRefreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRE, "刷新token已过期");
        }
        if (!redisRefreshToken.equals(token)) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID, "token已失效，请重新登陆");
        }

        UserVo userInfo = userService.getOneUserInfo(Long.valueOf(userId));
        TokenVo tokenVo = generateTokensAndSetToUser(userInfo);

        return tokenVo;
    }

    @Override
    public TokenVo generateTokensAndSetToUser(UserVo userInfo) {
        UserVo accessTokenUser = new UserVo()
                .setId(userInfo.getId())
                .setRoles(userInfo.getRoles());

        UserVo refreshTokenUser = new UserVo()
                .setId(userInfo.getId());

        TokenVo accessTokenVo = JwtUtils.generateAccessToken(accessTokenUser);
        TokenVo refreshTokenVo = JwtUtils.generateRefreshToken(refreshTokenUser);

        redisTemplate.opsForValue().set(
                "REFRESH_TOKEN_" + userInfo.getId(),
                refreshTokenVo.getRefreshToken(),
                JwtUtils.REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.SECONDS
        );

        return new TokenVo(
                accessTokenVo.getAccessToken(),
                refreshTokenVo.getRefreshToken(),
                accessTokenVo.getAccessExpires(),
                refreshTokenVo.getRefreshExpires());
    }
}
