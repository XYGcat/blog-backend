package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogCategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    /**
     * 条件分页查找分类列表
     *
     * @param request
     * @return
     */
    @PostMapping("/getCategoryList")
    public BaseResponse<PageInfoResult<BlogCategory>> getCategoryList(@RequestBody Map<String,Object> request){
        String category_name = (String) request.get("category_name");
        Integer current = (Integer) request.get("current");
        Integer size = (Integer) request.get("size");
        PageInfoResult<BlogCategory> categoryList = blogCategoryService.getCategoryList(category_name, current, size);
        return ResultUtils.success(categoryList,"分页查找分类成功");
    }

    /**
     * 新增分类
     *
     * @param request
     * @return
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<BlogCategory> addCategory(@RequestBody Map<String,Object> request){
        String category_name = (String) request.get("category_name");
        BlogCategory category = blogCategoryService.createCategory(category_name);
        return ResultUtils.success(category,"新增分类成功");
    }

    /**
     * 修改分类
     *
     * @param request
     * @return
     */
    @PutMapping("/update")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> updateCategory(@RequestBody Map<String,Object> request){
        String category_name = (String) request.get("category_name");
        Integer id = (Integer) request.get("id");
        Boolean aBoolean = blogCategoryService.updateCategory(id, category_name);
        return ResultUtils.success(aBoolean,"修改分类成功");
    }

    /**
     * 删除分类
     *
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> deleteCategories(@RequestBody Map<String,List<Integer>> request){
        List<Integer> categoryIdList = request.get("categoryIdList");
        Boolean aBoolean = blogCategoryService.deleteCategories(categoryIdList);
        return ResultUtils.success(aBoolean,"删除分类成功");
    }
}

