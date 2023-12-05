package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogArticle;
import com.xc.blogbackend.model.domain.request.ArticleRequest;
import com.xc.blogbackend.model.domain.result.ArticleListByContent;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.model.domain.result.RecommendResult;

import java.util.List;

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
    Boolean getArticleInfoByTitle(Integer id,String article_title);

    /**
     * 新增文章
     *
     * @param article
     * @return
     */
    BlogArticle createArticle(BlogArticle article);

    /**
     * 根据文章id获取文章详细信息
     *
     * @param article_id
     * @return
     */
    BlogArticle getArticleById(Integer article_id);

    /**
     * 修改文章信息
     *
     * @param blogArticle
     * @return
     */
    Boolean updateArticle(BlogArticle blogArticle);

    /**
     * 根据文章获取文章封面
     *
     * @param article_id
     * @return
     */
    String getArticleCoverById(Integer article_id);

    /**
     * 公开或隐藏文章
     *
     * @param id
     * @param status
     * @return
     */
    Boolean toggleArticlePublic(Integer id,Integer status);

    /**
     * 恢复文章
     *
     * @param id
     * @return
     */
    Boolean revertArticle(Integer id);

    /**
     * 删除文章
     *
     * @param id
     * @param status
     * @return
     */
    Boolean deleteArticle(Integer id,Integer status);

    /**
     *博客前台获取文章列表
     *
     * @param current
     * @param size
     * @return
     */
    PageInfoResult<BlogArticle> blogHomeGetArticleList(Integer current,Integer size);

    /**
     * 根据文章id获取推荐文章
     *
     * @param article_id
     * @return
     */
    RecommendResult getRecommendArticleById(Integer article_id);

    /**
     * 时间轴
     *
     * @param current
     * @param size
     * @return
     */
    PageInfoResult<BlogArticle> blogTimelineGetArticleList(Integer current, Integer size);

    /**
     * 通过分类id获取文章列表
     *
     * @param current
     * @param size
     * @param category_id
     * @return
     */
    PageInfoResult<BlogArticle> getArticleListByCategoryId(Integer current,Integer size,Integer category_id);

    /**
     * 通过tagId 获取到文章列表
     *
     * @param current
     * @param size
     * @param tag_id
     * @return
     */
    PageInfoResult<BlogArticle> getArticleListByTagId(Integer current,Integer size,Integer tag_id);

    /**
     * 获取热门文章
     *
     * @return
     */
    List<BlogArticle> getHotArticle();

    /**
     * 根据文章内容搜索文章
     *
     * @param content
     * @return
     */
    List<ArticleListByContent> getArticleListByContent(String content);

    /**
     * 文章点赞
     *
     * @param id
     * @return
     */
    Boolean articleLike(Integer id);

    /**
     * 取消文章点赞
     *
     * @param id
     * @return
     */
    Boolean cancelArticleLike(Integer id);

    /**
     * 文章增加阅读时长
     *
     * @param id
     * @param duration
     * @return
     */
    Boolean addReadingDuration(Integer id,Integer duration);

}
