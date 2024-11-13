package com.xc.blogbackend.model.domain.resDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分类实体返回类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResDto implements Serializable {

    private static final long serialVersionUID = 7684374508681609293L;

    /**
     *  id
     */
    private Long id;

    /**
     * 分类名称 唯一
     */
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

    /**
     * 子级分类
     */
    private List<CategoryResDto> subCategoryList;
}
