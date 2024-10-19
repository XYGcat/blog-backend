package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId
    private Integer id;

    /**
     * 资源名称
     */
    private String resource_name;

    /**
     * 资源图片
     */
    private String resource_img;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date created_at;

    /**
     * 更新时间
     */
    private Date updated_at;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}