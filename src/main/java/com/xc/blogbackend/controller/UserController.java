package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.BlogUser;
import com.xc.blogbackend.model.domain.request.UserLoginRequest;
import com.xc.blogbackend.model.domain.request.UserRegisterRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogUserService;
import com.xc.blogbackend.utils.IpUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<BlogUser> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        String ipAddress = IpUtils.getClientIp(request);
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
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Map<String,String>> userRegister(@RequestBody UserRegisterRequest userRegisterRequest,HttpServletRequest request) {
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(username,password,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //获取客户端ip
        String ip = IpUtils.getClientIp(request);
        Map<String,String> result = blogUserService.userRegister(username, password, checkPassword, ip);
        return ResultUtils.success(result);
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getUserInfoById/{id}")
    public BaseResponse<BlogUser> getUserInfo(@PathVariable Integer id){
        if (id != null) {
            if (id == 5201314) {
                BlogUser blogUser = new BlogUser();
                blogUser.setId(5201314);
                blogUser.setRole(1);
                blogUser.setNick_name("超级管理员");
                return ResultUtils.success(blogUser);
            } else {
                //// TODO: 2023-11-20 过滤返回值
                BlogUser userInfo = blogUserService.getOneUserInfo(id);
                return ResultUtils.success(userInfo);
            }
        }else{
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     * 分页获取用户列表
     *
     * @param request
     * @return
     */
    @PostMapping("/getUserList")
    public BaseResponse<PageInfoResult<BlogUser>> getUserList(@RequestBody Map<String,Object> request){
        Integer current = (Integer) request.get("current");
        String nick_name = (String) request.get("nick_name");
        Integer role = (Integer) request.get("role");
        Integer size = (Integer) request.get("size");
        PageInfoResult<BlogUser> userList = blogUserService.getUserList(current, nick_name, role, size);

        return ResultUtils.success(userList,"分页获取用户列表成功");
    }
}
