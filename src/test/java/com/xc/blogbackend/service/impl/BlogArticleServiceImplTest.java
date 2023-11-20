package com.xc.blogbackend.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlogArticleServiceImplTest {

    @Test
    void getArticleCount() {
        BlogArticleServiceImpl blogArticleService = new BlogArticleServiceImpl();
        long articleCount = blogArticleService.getArticleCount();

        // 断言查询结果是否为空
        assertNotNull(articleCount);
        assertEquals(1, articleCount);

        // 打印查询结果
        System.out.println("count" + articleCount);
    }
}