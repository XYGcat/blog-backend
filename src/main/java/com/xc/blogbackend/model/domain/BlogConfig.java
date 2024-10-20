package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName bg_config
 */
@TableName(value ="bg_config")
@Data
public class BlogConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 博客名称
     */
    @TableField(value = "bg_name")
    private String bg_name;

    /**
     * 博客头像
     */
    @TableField(value = "bg_avatar")
    private String bg_avatar;

    /**
     * 博客头像背景图
     */
    @TableField(value = "avatar_bg")
    private String avatar_bg;

    /**
     * 个人签名
     */
    @TableField(value = "personal_say")
    private String personal_say;

    /**
     * 博客公告
     */
    @TableField(value = "bg_notice")
    private String bg_notice;

    /**
     * qq链接
     */
    @TableField(value = "qq_link")
    private String qq_link;

    /**
     * 微信链接
     */
    @TableField(value = "we_chat_link")
    private String we_chat_link;

    /**
     * github链接
     */
    @TableField(value = "github_link")
    private String github_link;

    /**
     * git_ee链接
     */
    @TableField(value = "git_ee_link")
    private String git_ee_link;

    /**
     * bilibili链接
     */
    @TableField(value = "bilibili_link")
    private String bilibili_link;

    /**
     * 博客被访问的次数
     */
    @TableField(value = "view_time")
    private Long view_time;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}