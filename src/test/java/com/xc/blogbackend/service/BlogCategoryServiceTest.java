package com.xc.blogbackend.service;

import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.service.impl.BlogCategoryServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BlogCategoryServiceTest {

    @Test
    void getOneCategory() {

        BlogCategoryServiceImpl blogCategoryService = new BlogCategoryServiceImpl();

        // 创建一个测试用的分类名称
        String category_name = "后端";

        // 创建一个 BlogCategory 对象，用于存储查询结果
        BlogCategory category = blogCategoryService.getOneCategory(category_name);

        // 断言查询结果是否为空
        assertNotNull(category);
        assertEquals(category_name, category.getCategory_name());

        // 打印查询结果
        System.out.println("Category ID: " + category.getId());
        System.out.println("Category Name: " + category.getCategory_name());
    }
}