package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogCategoryMapper;
import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.service.BlogCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【blog_category】的数据库操作Service实现
* @createDate 2023-11-16 16:54:23
*/
@Service
@Slf4j
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory>
    implements BlogCategoryService{

    @Resource
    private BlogCategoryMapper blogCategoryMapper;

    @Override
    public String getCategoryNameById(Integer category_id) {
        BlogCategory category = blogCategoryMapper.selectById(category_id);
        return category != null ? category.getCategory_name() : null;
    }

    @Override
    public List<BlogCategory> getCategoryDictionary() {
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","category_name");
        List<BlogCategory> categories = blogCategoryMapper.selectList(queryWrapper);
        return categories;
    }

    @Override
    public BlogCategory getOneCategory(String category_name) {
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        if (category_name != null) {
            queryWrapper.eq("category_name", category_name);
        }
        queryWrapper.select("id", "category_name"); // 指定返回的列
        BlogCategory category = blogCategoryMapper.selectOne(queryWrapper);
        return category;
    }

    @Override
    public BlogCategory createCategory(String category_name) {
        BlogCategory category = new BlogCategory();
        category.setCategory_name(category_name);
        blogCategoryMapper.insert(category);

        Integer id = category.getId();
        return blogCategoryMapper.selectById(id);
    }

    @Override
    public Long getCategoryCount() {
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        Long count = blogCategoryMapper.selectCount(queryWrapper);

        return count;
    }
}




