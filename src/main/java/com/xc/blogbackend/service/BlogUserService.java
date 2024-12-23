package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.entity.BlogUser;
import com.xc.blogbackend.model.domain.resDto.PageInfoResult;
import com.xc.blogbackend.model.domain.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @author XC
* @description 针对表【bg_user】的数据库操作Service
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
    UserVo userLogin(String username, String password, String ip, HttpServletRequest request);

    /**
     * 用户注册
     *
     * @param username   用户账户
     * @param password  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    Map<String,String> userRegister(String username, String password, String checkPassword, String ip);

    /**
     * 根据id查询用户信息
     *
     * @param userId 用户id
     * @return
     */
    UserVo getOneUserInfo(Long userId);

    /**
     * 分页查询用户列表
     *
     * @param current
     * @param nickName
     * @param role
     * @param size
     * @return
     */
    PageInfoResult<BlogUser> getUserList(Integer current, String nickName, Integer role, Integer size);

    /**
     *获取用户总数
     *
     * @return
     */
    Long getUserCount();

    /**
     * 根据用户id获取昵称
     *
     * @param userId
     * @return
     */
    String getAuthorNameById(Long userId);

    /**
     * 用户自己修改用户信息
     *
     * @param request
     * @return
     */
    Boolean updateOwnUserInfo(Map<String,Object> request);

    /**
     * 修改用户密码
     *
     * @param id
     * @param password
     * @return
     */
    Boolean updatePassword(Long id,String password,String password1);

    /**
     * 修改用户角色
     *
     * @param id
     * @param role
     * @return
     */
    Boolean updateRole(Long id,Integer role);
}
