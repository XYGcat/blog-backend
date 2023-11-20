package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogCategory;

import java.util.List;

/**
* @author XC
* @description 针对表【blog_category】的数据库操作Service
* @createDate 2023-11-16 16:54:23
*/
public interface BlogCategoryService extends IService<BlogCategory> {

    /**
     * 通过分类id获取分类名称
     *
     * @param category_id
     * @return
     */
    String getCategoryNameById(int category_id);

    /**
     * 获取分类数据字典
     *
     * @return
     */
    List<BlogCategory> getCategoryDictionary();

    /**
     * 根据id或者分类名称获取分类信息
     *
     * @param category_name
     * @return
     */
    BlogCategory getOneCategory(String category_name);

    /**
     * 新增分类
     *
     * @param category_name
     * @return
     */
    BlogCategory createCategory(String category_name);
}
