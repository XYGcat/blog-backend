package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName bg_photo_album
 */
@TableName(value ="bg_photo_album")
@Data
public class BlogPhotoAlbum implements Serializable {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 相册名称
     */
    @TableField(value = "album_name")
    private String albumName;

    /**
     * 相册描述信息
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "created_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")    //保证在返回 JSON 格式数据时，日期字段按照指定格式展示
    private Date createdAt;

    /**
     * 
     */
    @TableField(value = "updated_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")    //保证在返回 JSON 格式数据时，日期字段按照指定格式展示
    private Date updatedAt;

    /**
     * 相册封面
     */
    @TableField(value = "album_cover")
    private String albumCover;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}