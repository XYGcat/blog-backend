package com.xc.blogbackend.model.domain.reqDto;

import lombok.Data;

/**
 * 分类请求参数
 */
@Data
public class CategoryReqDto {

    private String categoryName;

    /**
     * 分类类型 1:文章分类 2:资源导航分类
     */
    private Integer categoryType;

    /**
     * 分类级别（1: 一级, 2: 二级, 3: 三级）
     */
    private Integer level;

    /**
     * 父分类的ID（根分类为NULL）
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;
}
