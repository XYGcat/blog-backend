package com.xc.blogbackend.model.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName bg_comment
 */
@TableName(value ="bg_comment")
@Data
public class BlogComment implements Serializable {

    /**
     * 作者id
     */
    @TableField(exist = false)
    private Long authorId;

    /**
     * 是否点赞
     */
    @TableField(exist = false)
    private Boolean isLike;

    /**
     *地理位置
     */
    @TableField(exist = false)
    private String ipAddress;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 评论父级id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 评论的对象id 比如说说id、文章id等
     */
    @TableField(value = "for_id")
    private Long forId;

    /**
     * 评论类型 1 文章 2 说说 3 留言 ...
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 评论人id
     */
    @TableField(value = "from_id")
    private Long fromId;

    /**
     * 评论人昵称
     */
    @TableField(value = "from_name")
    private String fromName;

    /**
     * 评论人头像
     */
    @TableField(value = "from_avatar")
    private String fromAvatar;

    /**
     * 被回复的人id
     */
    @TableField(value = "to_id")
    private Long toId;

    /**
     * 被回复人的昵称
     */
    @TableField(value = "to_name")
    private String toName;

    /**
     * 被回复人的头像
     */
    @TableField(value = "to_avatar")
    private String toAvatar;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 评论点赞数
     */
    @TableField(value = "thumbs_up")
    private Integer thumbsUp;

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

    /**
     * ip地址
     */
    @TableField(value = "ip")
    private String ip;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}