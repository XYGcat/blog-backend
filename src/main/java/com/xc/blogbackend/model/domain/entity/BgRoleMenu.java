package com.xc.blogbackend.model.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色菜单关系表
 * @TableName bg_role_menu
 */
@TableName(value ="bg_role_menu")
@Data
public class BgRoleMenu implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 角色ID
     */
    private Long role_id;

    /**
     * 菜单ID
     */
    private Long menu_id;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}