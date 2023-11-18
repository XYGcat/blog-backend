package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.mapper.BlogCategoryMapper;
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
    public String getCategoryNameById(int category_id) {
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
}




