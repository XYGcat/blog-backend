package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName bg_message
 */
@TableName(value ="bg_message")
@Data
public class BlogMessage implements Serializable {

    //是否点赞
    @TableField(exist = false)
    private Boolean  isLike;

    /**
     *评论总数
     */
    @TableField(exist = false)
    private Long commentTotal;

    /**
     * 用户头像
     */
    @TableField(exist = false)
    private String avatar;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标签
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 留言内容
     */
    @TableField(value = "message")
    private String message;

    /**
     * 字体颜色
     */
    @TableField(value = "color")
    private String color;

    /**
     * 字体大小
     */
    @TableField(value = "font_size")
    private Integer fontSize;

    /**
     * 背景颜色
     */
    @TableField(value = "bg_color")
    private String bgColor;

    /**
     * 背景图片
     */
    @TableField(value = "bg_url")
    private String bgUrl;

    /**
     * 留言用户的id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 点赞次数
     */
    @TableField(value = "like_times")
    private Integer likeTimes;

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
     * 字体宽度
     */
    @TableField(value = "font_weight")
    private Integer fontWeight;

    /**
     * 游客用户的昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}