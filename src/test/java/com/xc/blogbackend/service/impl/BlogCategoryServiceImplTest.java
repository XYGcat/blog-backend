package com.xc.blogbackend.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BlogCategoryServiceImplTest {

    @Test
    void getCategoryNameById() {
        BlogCategoryServiceImpl blogCategoryService = new BlogCategoryServiceImpl();

        // 创建一个测试用的分类id
        Integer category_id = 4;

        // 创建一个 BlogCategory 对象，用于存储查询结果
        String categoryNameById = blogCategoryService.getCategoryNameById(category_id);

        // 断言查询结果是否为空
        assertNotNull(categoryNameById);
        assertEquals(category_id, categoryNameById);

        // 打印查询结果
        System.out.println("Category Name: " + categoryNameById);
    }
}