package com.xc.blogbackend.model.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色表
 * @TableName bg_user_role
 */
@TableName(value ="bg_user_role")
@Data
public class BgUserRole implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 角色ID
     */
    private Long role_id;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}