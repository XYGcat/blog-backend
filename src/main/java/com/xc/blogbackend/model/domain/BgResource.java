package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName bg_resource
 */
@TableName(value ="bg_resource")
@Data
public class BgResource implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 网站名称
     */
    private String siteName;

    /**
     * 网站链接
     */
    private String siteUrl;

    /**
     * 网站图片
     */
    private String siteImg;

    /**
     * 网站描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间，插入时自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdAt;

    /**
     * 更新时间
     */
    /**
     * 更新时间，插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}