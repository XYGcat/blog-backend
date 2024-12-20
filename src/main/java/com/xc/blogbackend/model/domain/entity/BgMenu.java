package com.xc.blogbackend.model.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单表
 * @TableName bg_menu
 */
@TableName(value ="bg_menu")
@Data
public class BgMenu implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 菜单编码
     */
    private String menu_code;

    /**
     * 父节点
     */
    private Long parent_id;

    /**
     * 节点类型，1文件夹，2页面，3按钮
     */
    private Integer node_type;

    /**
     * 图标地址
     */
    private String icon_url;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 页面对应的地址
     */
    private String link_url;

    /**
     * 层次
     */
    private Integer level;

    /**
     * 树id的路径 整个层次上的路径id，逗号分隔，想要找父节点特别快
     */
    private String path;

    /**
     * 是否删除 1：已删除；0：未删除
     */
    private Integer is_delete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}