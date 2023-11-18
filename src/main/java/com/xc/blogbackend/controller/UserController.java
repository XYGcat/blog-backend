package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.BlogUser;
import com.xc.blogbackend.model.domain.request.UserLoginRequest;
import com.xc.blogbackend.model.domain.request.UserRegisterRequest;
import com.xc.blogbackend.service.BlogUserService;
import com.xc.blogbackend.utils.GetClientIp;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.xc.blogbackend.contant.BlogUserConstant.ADMIN_PASSWORD;
import static com.xc.blogbackend.utils.SecretKeyUtil.SECRET_KEY_BYTES;

/**
 *用户接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private BlogUserService blogUserService;

    /**
     * 登录接口
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<BlogUser> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        String ipAddress = GetClientIp.getClientIp(request);
        if(userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if(StringUtils.isAnyBlank(username,password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (username.equals("admin")) {
            if (password.equals(ADMIN_PASSWORD)) {
                // 生成token
                String token = Jwts.builder()
                        .claim("nick_name", "超级管理员")
                        .claim("id", 5201314)
                        .claim("role", 1)
                        .claim("username", "admin")
                        .signWith(Keys.hmacShaKeyFor(SECRET_KEY_BYTES))
                        .compact();
                BlogUser blogUser = new BlogUser();
                blogUser.setUsername("超级管理员");
                blogUser.setRole(1);
                blogUser.setId(5201314);
                blogUser.setIp(ipAddress);
                blogUser.setToken(token);

                return ResultUtils.success(blogUser);
            } else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }else{
            BlogUser blogUser = blogUserService.userLogin(username, password,ipAddress, request);

            // 生成token
            String token = Jwts.builder()
                    .claim("nick_name", blogUser.getNick_name())
                    .claim("id", blogUser.getId())
                    .claim("role", blogUser.getRole())
                    .claim("username", blogUser.getUsername())
                    .signWith(Keys.hmacShaKeyFor(SECRET_KEY_BYTES))
                    .compact();
            blogUser.setToken(token);

            return ResultUtils.success(blogUser);
        }
    }

    /**
     * 注册接口
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest,HttpServletRequest request) throws IOException {
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(username,password,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = blogUserService.userRegister(username, password, checkPassword);
        return ResultUtils.success(result);
    }

    @GetMapping("/getUserInfoById/{id}")
    public BaseResponse<BlogUser> getUserInfoById(@PathVariable Long id){
        if (id != null) {
            if (id == 5201314) {
                BlogUser blogUser = new BlogUser();
                blogUser.setId(5201314);
                blogUser.setRole(1);
                blogUser.setNick_name("超级管理员");
                return ResultUtils.success(blogUser);
            } else {
                BlogUser userInfo = blogUserService.getOneUserInfo(id);
                return ResultUtils.success(userInfo);
            }
        }else{
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }
}
