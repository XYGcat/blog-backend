package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogCategoryMapper;
import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public String getCategoryNameById(Integer category_id) {
        BlogCategory category = blogCategoryMapper.selectById(category_id);
        return category != null ? category.getCategory_name() : null;
    }

    @Override
    public List<BlogCategory> getCategoryDictionary(Integer type) {
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","category_name");
        queryWrapper.eq("category_type",type);
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
    public BlogCategory createCategory(String category_name, Integer type) {
        BlogCategory category = new BlogCategory();
        category.setCategory_name(category_name);
        category.setCategoryType(type);
        blogCategoryMapper.insert(category);

        Integer id = category.getId();
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
    public PageInfoResult<BlogCategory> getCategoryList(String category_name, Integer current, Integer size) {

        // 构建查询条件
        QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
        // 如果分类名不为空，使用like模糊查询
        if (category_name != null && !category_name.isEmpty()) {
            queryWrapper.like("category_name", "%" + category_name + "%");
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
    public Boolean updateCategory(Integer id, String category_name) {
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategory_name(category_name);
        UpdateWrapper<BlogCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        int update = blogCategoryMapper.update(blogCategory, updateWrapper);
        return update > 0;
    }

    @Override
    public Boolean deleteCategories(List<Integer> idList) {
        int batchIds = blogCategoryMapper.deleteBatchIds(idList);
        return batchIds > 0;
    }
}




