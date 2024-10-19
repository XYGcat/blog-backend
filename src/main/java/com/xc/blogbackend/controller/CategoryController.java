package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 文章分类接口
 *
 * @author 星尘
 */
@Api(tags = "文章分类接口")
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
    @ApiOperation(value = "根据类型获取分类字典")
    @GetMapping("/getCategoryDictionary/{type}")
    public BaseResponse<List<BlogCategory>> getCategoryDictionary(@PathVariable Integer type){
        List<BlogCategory> categoryDictionary = blogCategoryService.getCategoryDictionary(type);
        return ResultUtils.success(categoryDictionary);
    }

    /**
     * 条件分页查找分类列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "条件分页查找分类列表")
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
    @ApiOperation(value = "新增分类")
    @PostMapping("/add")
    public BaseResponse<BlogCategory> addCategory(@RequestBody Map<String,Object> request){
        String category_name = (String) request.get("category_name");
        Integer type = (Integer) request.get("type");
        BlogCategory category = blogCategoryService.createCategory(category_name, type);
        return ResultUtils.success(category,"新增分类成功");
    }

    /**
     * 修改分类
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "修改分类")
    @PutMapping("/update")
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
    @ApiOperation(value = "删除分类")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCategories(@RequestBody Map<String,List<Integer>> request){
        List<Integer> categoryIdList = request.get("categoryIdList");
        Boolean aBoolean = blogCategoryService.deleteCategories(categoryIdList);
        return ResultUtils.success(aBoolean,"删除分类成功");
    }
}

