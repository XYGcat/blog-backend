package com.xc.blogbackend.model.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色表
 * @TableName bg_role
 */
@TableName(value ="bg_role")
@Data
public class BgRole implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否删除 1：已删除；0：未删除
     */
    private Integer is_delete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}