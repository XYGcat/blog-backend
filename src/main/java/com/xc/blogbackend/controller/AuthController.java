package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.enums.ErrorCode;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.vo.TokenVo;
import com.xc.blogbackend.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户认证接口
 */
@Api(tags = "用户认证接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 刷新令牌接口
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "刷新令牌接口")
    @PostMapping("/refreshToken")
    public BaseResponse<TokenVo> refreshAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(authorization)) {
            throw new BusinessException(ErrorCode.NO_LOGIN,"您没有权限访问，请先登录");
        }

        //检查并提取有效的 JWT 令牌内容
        String token = authorization.startsWith("Bearer ")
                ? authorization.replace("Bearer ", "")
                : authorization;

        TokenVo tokenVo = authService.refreshAccessToken(token);

        return ResultUtils.success(tokenVo);
    }
}
