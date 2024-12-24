package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.entity.BlogCategory;
import com.xc.blogbackend.model.domain.reqDto.CategoryReqDto;
import com.xc.blogbackend.model.domain.resDto.CategoryResDto;
import com.xc.blogbackend.model.domain.resDto.PageInfoResult;

import java.util.List;
import java.util.Map;

/**
* @author XC
* @description 针对表【bg_category】的数据库操作Service
* @createDate 2023-11-16 16:54:23
*/
public interface BlogCategoryService extends IService<BlogCategory> {

    /**
     * 通过分类id获取分类名称
     *
     * @param categoryId
     * @return
     */
    String getCategoryNameById(Long categoryId);

    /**
     * 通过分类id列表获取分类信息
     *
     * @param categoryIds
     * @return
     */
    List<BlogCategory> getCategoryByIds(List<Long> categoryIds);

    /**
     * 根据参数获取分类数据字典
     *
     * @return
     */
    List<CategoryResDto> getCategoryDictionary(Map<String,String> params);

    /**
     * 根据id或者分类名称获取分类信息
     *
     * @param categoryName
     * @return
     */
    BlogCategory getOneCategory(String categoryName);

    /**
     * 新增分类
     *
     * @param categoryReqDto
     * @return
     */
    BlogCategory createCategory(CategoryReqDto categoryReqDto);

    /**
     *获取分类总数
     *
     * @return
     */
    Long getCategoryCount();

    /**
     * 分页获取分类列表
     *
     * @param categoryName
     * @param current
     * @param size
     * @return
     */
    PageInfoResult<BlogCategory> getCategoryList(String categoryName,Integer current,Integer size);

    /**
     * 修改分类
     *
     * @param id
     * @param categoryName
     * @return
     */
    Boolean updateCategory(Long id,String categoryName);

    /**
     * 删除分类
     *
     * @param idList
     * @return
     */
    Boolean deleteCategories(List<Long> idList);

    /**
     * 获取子分类
     *
     * @param categoryIdList
     * @return
     */
    List<CategoryResDto> getSubCategoryList(List<Long> categoryIdList);
}
