package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.enums.ErrorCode;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.reqDto.loginReqDto;
import com.xc.blogbackend.model.domain.vo.MenuVo;
import com.xc.blogbackend.model.domain.vo.UserVo;
import com.xc.blogbackend.service.BgMenuService;
import com.xc.blogbackend.service.BlogUserService;
import com.xc.blogbackend.utils.IpUtils;
import com.xc.blogbackend.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户认证接口
 */
@Api(tags = "用户认证接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RedisTemplate redisTemplate;
    private final BlogUserService userService;
    private final BgMenuService menuService;

    /**
     * 刷新令牌接口
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "刷新令牌接口")
    @GetMapping("/refreshToken")
    public BaseResponse<UserVo> refreshAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(authorization)) {
            throw new BusinessException(ErrorCode.NO_LOGIN,"您没有权限访问，请先登录");
        }

        //检查并提取有效的 JWT 令牌内容
        String token = authorization.startsWith("Bearer ")
                ? authorization.replace("Bearer ", "")
                : authorization;

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
        generateTokensAndSetToUser(userInfo);

        return ResultUtils.success(userInfo);
    }

    /**
     * 登录接口
     *
     * @param loginReqDto
     * @param request
     * @return
     */
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public BaseResponse<UserVo> userLogin(@RequestBody loginReqDto loginReqDto, HttpServletRequest request){
        String ipAddress = IpUtils.getClientIp(request);
        String username = loginReqDto.getUsername();
        String password = loginReqDto.getPassword();

        UserVo userInfo = userService.userLogin(username, password,ipAddress, request);
        generateTokensAndSetToUser(userInfo);

        return ResultUtils.success(userInfo);
    }

    /**
     * 登出接口
     *
     * @return
     */
    @ApiOperation(value = "登出接口")
    @PostMapping("/logout")
    public BaseResponse<UserVo> logout(@RequestBody String userId) {
        redisTemplate.delete("REFRESH_TOKEN_" + userId);
        List<MenuVo> menuVos = menuService.roleQueryMenus(null);
        UserVo userVo = new UserVo().setMenus(menuVos);
        return ResultUtils.success(userVo);
    }

    /**
     * 公共方法：生成Token并设置到UserVo中
     */
    private void generateTokensAndSetToUser(UserVo userInfo) {
        UserVo accessTokenUser = new UserVo()
                .setId(userInfo.getId())
                .setRoles(userInfo.getRoles());

        UserVo refreshTokenUser = new UserVo()
                .setId(userInfo.getId());

        String accessToken = JwtUtils.generateAccessToken(accessTokenUser);
        String refreshToken = JwtUtils.generateRefreshToken(refreshTokenUser);

        redisTemplate.opsForValue().set(
                "REFRESH_TOKEN_" + userInfo.getId(),
                refreshToken,
                JwtUtils.REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.SECONDS
        );

        userInfo.setAccessToken(accessToken);
        userInfo.setRefreshToken(refreshToken);
    }
}
