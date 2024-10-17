package com.xc.blogbackend.controller;

import com.qiniu.common.QiniuException;
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
import com.xc.blogbackend.utils.JwtGenerator;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import static com.xc.blogbackend.contant.BlogUserConstant.ADMIN_PASSWORD;
import static com.xc.blogbackend.contant.BlogUserConstant.USER_LOGIN_STATE;

/**
 *用户接口
 *
 * @author 星尘
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private Qiniu qiniu;

    /**
     * 登录接口
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public BaseResponse<BlogUser> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        String ipAddress = IpUtils.getClientIp(request);
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if(StringUtils.isAnyBlank(username,password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入账号密码！");
        }
        if (username.equals("admin")) {
            if (password.equals(ADMIN_PASSWORD)) {

                BlogUser blogUser = new BlogUser();
                blogUser.setUsername("admin");
                blogUser.setNick_name("超级管理员");
                blogUser.setRole(1);
                blogUser.setId(5201314);
                blogUser.setIp(ipAddress);

                // 生成token
                String token = JwtGenerator.generateToken(blogUser);
                blogUser.setToken(token);

                return ResultUtils.success(blogUser);
            } else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"管理员账号/密码错误！");
            }
        }else{
            BlogUser blogUser = blogUserService.userLogin(username, password,ipAddress, request);

            //创建Token
            String token = JwtGenerator.generateToken(blogUser);
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
    @ApiOperation(value = "注册接口")
    @PostMapping("/register")
    public BaseResponse<Map<String,String>> userRegister(@RequestBody UserRegisterRequest userRegisterRequest,HttpServletRequest request) {
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入账号密码！");
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
    @ApiOperation(value = "根据用户id获取用户信息")
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

                //添加七牛云访问Token
                try {
                    String url = qiniu.downloadUrl(userInfo.getAvatar());
                    userInfo.setAvatar(url);
                } catch (QiniuException e) {
                    throw new RuntimeException(e);
                }

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
    @ApiOperation(value = "分页获取用户列表")
    @PostMapping("/getUserList")
    public BaseResponse<PageInfoResult<BlogUser>> getUserList(@RequestBody Map<String,Object> request){
        Integer current = (Integer) request.get("current");
        String nick_name = (String) request.get("nick_name");
        Integer role = (Integer) request.get("role");
        Integer size = (Integer) request.get("size");
        PageInfoResult<BlogUser> userList = blogUserService.getUserList(current, nick_name, role, size);

        return ResultUtils.success(userList,"分页获取用户列表成功");
    }

    /**
     * 更新用户信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "更新用户信息")
    @PutMapping("/updateOwnUserInfo")
    public BaseResponse<Boolean> updateOwnUserInfo(@RequestBody Map<String,Object> request){
        Integer id = (Integer) request.get("id");
        String avatar = (String) request.get("avatar");

        BlogUser userInfo = blogUserService.getOneUserInfo(id);

        // 服务器删除原来的头像
        if (userInfo.getAvatar() != null && userInfo.getAvatar() != avatar) {
            String subString = StringManipulation.subString(userInfo.getAvatar());
            Boolean aBoolean = qiniu.deleteFile(subString);
        }

        Boolean aBoolean = blogUserService.updateOwnUserInfo(request);

        return ResultUtils.success(aBoolean,"修改用户成功");
    }

    /**
     * 修改密码
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("/updatePassword")
    public BaseResponse<Boolean> updatePassword(@RequestBody Map<String, String> request,HttpServletRequest httpServletRequest){
        String password = request.get("password");
        String password1 = request.get("password1");
        String password2 = request.get("password2");

        // 从 Session 中获取 BlogUser 对象
        BlogUser safetyUser = (BlogUser) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        Integer id = safetyUser.getId();

        Boolean aBoolean = blogUserService.updatePassword(id, password, password1);

        return ResultUtils.success(aBoolean,"修改用户密码成功");
    }

    /**
     * 修改用户角色
     *
     * @param id
     * @param role
     * @return
     */
    @ApiOperation(value = "修改用户角色")
    @PutMapping("/updateRole/{id}/{role}")
    public BaseResponse<Boolean> updateRole(@PathVariable Integer id,@PathVariable Integer role){
        Boolean aBoolean = blogUserService.updateRole(id, role);

        return ResultUtils.success(aBoolean,"修改角色成功");
    }
}
