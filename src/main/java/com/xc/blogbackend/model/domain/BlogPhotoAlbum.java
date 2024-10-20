package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
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

    /**
     * 相册封面
     */
    @TableField(value = "album_cover")
    private String albumCover;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}