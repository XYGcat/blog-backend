package com.xc.blogbackend.model.domain.resDto;

import com.xc.blogbackend.model.domain.vo.MenuVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 登录返回对象
 */
@Data
@ApiModel("登录返回对象")
public class LoginResDto implements Serializable {

    private static final long serialVersionUID = 8707436132498367035L;

    /**
     * 主键
     */
    private Long id;

    /**
     * ip所在地理位置
     */
    private String ipAddress;

    /**
     * 添加一个token字段
     */
    private String token;

    /**
     * 账号，唯一
     */
    private String username;

    /**
     * 用户角色 1 管理员 2 普通用户
     */
    private Integer role;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户QQ 用于联系
     */
    private String qq;

    /**
     * ip属地
     */
    private String ip;

    /**
     * 用户菜单
     */
    private List<MenuVo> menus;
}
