package com.xc.blogbackend.model.domain.vo;

import lombok.Data;

/**
 * 图片 vo
 */
@Data
public class ImageVo{

    /**
     * 图片链接
     */
    private String imageUrl;

    /**
     * 图片尺寸
     */
    private String imageSize;

    /**
     * 图片文件大小
     */
    private Long imageFileLength;
}

