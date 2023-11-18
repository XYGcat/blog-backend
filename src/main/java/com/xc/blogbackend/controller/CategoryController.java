package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.service.BlogCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章分类接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private BlogCategoryService blogCategoryService;

    /**
     * 获取分类字典
     *
     * @return
     */
    @GetMapping("/getCategoryDictionary")
    public BaseResponse<List<BlogCategory>> getCategoryDictionary(){
        List<BlogCategory> categoryDictionary = blogCategoryService.getCategoryDictionary();
        return ResultUtils.success(categoryDictionary);
    }

}

