package com.xc.blogbackend.model.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName bg_article_tag
 */
@TableName(value ="bg_article_tag")
@Data
public class BlogArticleTag implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文章id
     */
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * 标签id
     */
    @TableField(value = "tag_id")
    private Long tagId;

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