package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName bg_category
 */
@TableName(value ="bg_category")
@Data
public class BlogCategory implements Serializable {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称 唯一
     */
    @TableField(value = "category_name")
    private String categoryName;

    /**
     * 分类类型 1:文章分类 2:资源导航分类
     */
    @TableField(value = "category_type")
    private Integer categoryType;

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
     * 更新时间，插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}