package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogArticle;
import com.xc.blogbackend.model.domain.request.ArticleRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

/**
* @author XC
* @description 针对表【blog_article】的数据库操作Service
* @createDate 2023-11-16 01:29:08
*/
public interface BlogArticleService extends IService<BlogArticle> {

    /**
     * 获取文章总数
     *
     * @return 已发表的文章数
     */
    long getArticleCount();

    /**
     * 条件分页查询文章列表
     *
     * @param articleRequest
     * @return
     */
    PageInfoResult<BlogArticle> getArticleList(ArticleRequest articleRequest);

    /**
     * 根据文章标题获取文章信息 校验是否可以新增或编辑文章
     *
     * @param id
     * @param article_title
     * @return
     */
    Boolean getArticleInfoByTitle(String id,String article_title);

    /**
     * 新增文章
     *
     * @param article
     * @return
     */
    BlogArticle createArticle(BlogArticle article);

//    /**
//     * 获取标签总数
//     *
//     * @return
//     */
//    BlogArticle getTagCount();
//
//    /**
//     *获取分类总数
//     *
//     * @return
//     */
//    BlogArticle getCategoryCount();
//
//    /**
//     *获取用户总数
//     *
//     * @return
//     */
//    BlogArticle getUserCount();
}
