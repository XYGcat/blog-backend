package com.xc.blogbackend.model.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 博客名称
     */
    @TableField(value = "bg_name")
    private String bgName;

    /**
     * 博客头像
     */
    @TableField(value = "bg_avatar")
    private String bgAvatar;

    /**
     * 博客头像背景图
     */
    @TableField(value = "avatar_bg")
    private String avatarBg;

    /**
     * 个人签名
     */
    @TableField(value = "personal_say")
    private String personalSay;

    /**
     * 博客公告
     */
    @TableField(value = "bg_notice")
    private String bgNotice;

    /**
     * qq链接
     */
    @TableField(value = "qq_link")
    private String qqLink;

    /**
     * 微信链接
     */
    @TableField(value = "we_chat_link")
    private String weChatLink;

    /**
     * github链接
     */
    @TableField(value = "github_link")
    private String githubLink;

    /**
     * gitee链接
     */
    @TableField(value = "gitee_link")
    private String giteeLink;

    /**
     * bilibili链接
     */
    @TableField(value = "bili_link")
    private String biliLink;

    /**
     * 博客被访问的次数
     */
    @TableField(value = "view_time")
    private Long viewTime;

    /**
     * 创建时间，插入时自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createdAt;

    /**
     * 更新时间，插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}