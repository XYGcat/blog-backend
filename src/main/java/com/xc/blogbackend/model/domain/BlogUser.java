package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName bg_user
 */
@TableName(value ="bg_user")
@Data
public class BlogUser implements Serializable {

    /**
     * ip所在地理位置
     */
    @TableField(exist = false)
    private String ip_address;

    /**
     * 添加一个token字段
     */
    @TableField(exist = false)
    private String token;

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号，唯一
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户角色 1 管理员 2 普通用户
     */
    @TableField(value = "role")
    private Integer role;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    private String nick_name;

    /**
     * 用户头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 创建时间，插入时自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdAt;

    /**
     * 更新时间，插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updatedAt;

    /**
     * 用户QQ 用于联系
     */
    @TableField(value = "qq")
    private String qq;

    /**
     * ip属地
     */
    @TableField(value = "ip")
    private String ip;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}