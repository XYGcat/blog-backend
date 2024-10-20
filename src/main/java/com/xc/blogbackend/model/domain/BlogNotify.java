package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName bg_notify
 */
@TableName(value ="bg_notify")
@Data
public class BlogNotify implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 通知内容
     */
    @TableField(value = "message")
    private String message;

    /**
     * 通知给谁
     */
    @TableField(value = "user_id")
    private Integer user_id;

    /**
     * 通知类型 1 文章 2 说说 3 留言 4 友链
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 说说或者是文章的id 用于跳转
     */
    @TableField(value = "to_id")
    private Integer to_id;

    /**
     * 是否被查看 1 没有 2 已经查看
     */
    @TableField(value = "isView")
    private Integer isView;

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