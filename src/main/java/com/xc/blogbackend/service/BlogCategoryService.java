package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

import java.util.List;

/**
* @author XC
* @description 针对表【bg_category】的数据库操作Service
* @createDate 2023-11-16 16:54:23
*/
public interface BlogCategoryService extends IService<BlogCategory> {

    /**
     * 通过分类id获取分类名称
     *
     * @param category_id
     * @return
     */
    String getCategoryNameById(Integer category_id);

    /**
     * 根据类型获取分类数据字典
     *
     * @return
     */
    List<BlogCategory> getCategoryDictionary(Integer type);

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
     * @param categoryName
     * @return
     */
    BlogCategory createCategory(String categoryName, Integer type);

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
    Boolean updateCategory(Integer id,String categoryName);

    /**
     * 删除分类
     *
     * @param idList
     * @return
     */
    Boolean deleteCategories(List<Integer> idList);
}
