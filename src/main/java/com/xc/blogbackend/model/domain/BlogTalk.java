package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @TableName bg_talk
 */
@TableName(value ="bg_talk")
@Data
public class BlogTalk implements Serializable {

    @TableField(exist = false)
    private Boolean  is_like;

    @TableField(exist = false)
    private String  avatar;

    @TableField(exist = false)
    private String nick_name;

    @TableField(exist = false)
    private List<String> talkImgListResponse;

    @TableField(exist = false)
    private List<Map<String,String>> talkImgList;   //添加说说时的图片数据列表请求体

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发布说说的用户id
     */
    @TableField(value = "user_id")
    private Integer user_id;

    /**
     * 说说内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 说说状态 1 公开 2 私密 3 回收站
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否置顶 1 置顶 2 不置顶
     */
    @TableField(value = "is_top")
    private Integer is_top;

    /**
     * 点赞次数
     */
    @TableField(value = "like_times")
    private Integer like_times;

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