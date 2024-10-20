package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName bg_links
 */
@TableName(value ="bg_links")
@Data
public class BlogLinks implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 网站名称
     */
    @TableField(value = "site_name")
    private String site_name;

    /**
     * 网站描述
     */
    @TableField(value = "site_desc")
    private String site_desc;

    /**
     * 网站头像
     */
    @TableField(value = "site_avatar")
    private String site_avatar;

    /**
     * 网站地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 友链状态 1 待审核 2 审核通过
     */
    @TableField(value = "status")
    private Integer status;

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