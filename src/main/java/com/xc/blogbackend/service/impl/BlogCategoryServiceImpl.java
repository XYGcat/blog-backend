package com.xc.blogbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogCategoryMapper;
import com.xc.blogbackend.model.domain.entity.BlogCategory;
import com.xc.blogbackend.model.domain.reqDto.CategoryReqDto;
import com.xc.blogbackend.model.domain.resDto.CategoryResDto;
import com.xc.blogbackend.model.domain.resDto.PageInfoResult;
import com.xc.blogbackend.service.BlogCategoryService;
import com.xc.blogbackend.utils.PropertyCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author XC
* @description 针对表【bg_category】的数据库操作Service实现
* @createDate 2023-11-16 16:54:23
*/
@Service
@Slf4j
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory>
    implements BlogCategoryService{

    @Resource
    private BlogCategoryMapper blogCategoryMapper;

    @Override
    public String getCategoryNameById(Long categoryId) {
        BlogCategory category = blogCategoryMapper.selectById(categoryId);
        return category != null ? category.getCategoryName() : null;
    }

    @Override
    public List<CategoryResDto> getCategoryDictionary(Map<String,String> params) {
        Integer categoryType = Integer.valueOf(params.get("categoryType"));
        Integer level = Integer.valueOf(params.get("level"));
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","category_name","category_type");
        queryWrapper.orderByAsc("sort");
        // 根据参数进行查询条件设置
        Optional.ofNullable(categoryType).ifPresent(t -> queryWrapper.eq("category_type", t));
        Optional.ofNullable(level).ifPresent(l -> queryWrapper.eq("level", l));

        List<BlogCategory> categories = blogCategoryMapper.selectList(queryWrapper);
        // 将查询结果转换为CategoryResDto
        List<CategoryResDto> categoryResDtos = PropertyCopyUtils.copyProperties(categories, CategoryResDto.class);
        // 获取子分类
        List<Long> categoryIdList = categoryResDtos.stream().map(CategoryResDto::getId).collect(Collectors.toList());
        List<CategoryResDto> subCategoryList = getSubCategoryList(categoryIdList);
        Map<Long, List<CategoryResDto>> parentIdList = subCategoryList.stream()
                .collect(Collectors.groupingBy(CategoryResDto::getParentId));
        // 设置子分类
        categoryResDtos.forEach(categoryResDto -> {
            List<CategoryResDto> subCategory = parentIdList.get(categoryResDto.getId());
            if (CollUtil.isNotEmpty(subCategory)){
                categoryResDto.setSubCategoryList(subCategory);
            }
        });

        return categoryResDtos;
    }

    @Override
    public BlogCategory getOneCategory(String categoryName) {
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        if (categoryName != null) {
            queryWrapper.eq("category_name", categoryName);
        }
        queryWrapper.select("id", "category_name"); // 指定返回的列
        BlogCategory category = blogCategoryMapper.selectOne(queryWrapper);
        return category;
    }

    @Override
    public BlogCategory createCategory(CategoryReqDto categoryReqDto) {
        BlogCategory category = new BlogCategory();
        category.setCategoryName(categoryReqDto.getCategoryName());
        category.setCategoryType(categoryReqDto.getCategoryType());
        category.setLevel(categoryReqDto.getLevel());
        category.setParentId(categoryReqDto.getParentId());
        category.setSort(categoryReqDto.getSort());
        blogCategoryMapper.insert(category);

        Long id = category.getId();
        BlogCategory blogCategory = blogCategoryMapper.selectById(id);
        return blogCategory;
    }

    @Override
    public Long getCategoryCount() {
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        Long count = blogCategoryMapper.selectCount(queryWrapper);

        return count;
    }

    @Override
    public PageInfoResult<BlogCategory> getCategoryList(String categoryName, Integer current, Integer size) {

        // 构建查询条件
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        // 如果分类名不为空，使用like模糊查询
        if (categoryName != null && !categoryName.isEmpty()) {
            queryWrapper.like("category_name", "%" + categoryName + "%");
        }

        // 创建Page对象，设置当前页和分页大小
        Page<BlogCategory> page = new Page<>(current, size);
        // 获取分类列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogCategory> categoryPage = blogCategoryMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogCategory> rows = categoryPage.getRecords();
        // 获取分类总数
        long count = categoryPage.getTotal();

        PageInfoResult<BlogCategory> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setSize(size);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }

    @Override
    public Boolean updateCategory(Long id, String categoryName) {
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategoryName(categoryName);
        UpdateWrapper<BlogCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        int update = blogCategoryMapper.update(blogCategory, updateWrapper);
        return update > 0;
    }

    @Override
    public Boolean deleteCategories(List<Long> idList) {
        int batchIds = blogCategoryMapper.deleteByIds(idList);
        return batchIds > 0;
    }

    @Override
    public List<CategoryResDto> getSubCategoryList(List<Long> categoryIdList) {
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("parent_id", categoryIdList);
        queryWrapper.orderByAsc("sort");
        List<BlogCategory> subCategories = blogCategoryMapper.selectList(queryWrapper);

        // 将BlogCategory转换为CategoryResDto
        List<CategoryResDto> categoryResDtos = PropertyCopyUtils.copyProperties(subCategories, CategoryResDto.class);

        return categoryResDtos;
    }
}




