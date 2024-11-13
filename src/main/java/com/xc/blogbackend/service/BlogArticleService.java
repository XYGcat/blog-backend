package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.entity.BlogArticle;
import com.xc.blogbackend.model.domain.reqDto.ArticleRequest;
import com.xc.blogbackend.model.domain.reqDto.UpdateArticleRequest;
import com.xc.blogbackend.model.domain.resDto.ArticleListByContent;
import com.xc.blogbackend.model.domain.resDto.ArticleResDto;
import com.xc.blogbackend.model.domain.resDto.PageInfoResult;
import com.xc.blogbackend.model.domain.resDto.RecommendResult;

import java.util.List;

/**
* @author XC
* @description 针对表【bg_article】的数据库操作Service
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
     * @param articleTitle
     * @return
     */
    Boolean getArticleInfoByTitle(Long id,String articleTitle);

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
     * @param articleId
     * @return
     */
    BlogArticle getArticleById(Long articleId);

    /**
     * 根据文章id获取文章内容的图片链接
     *
     * @param articleId
     * @return
     */
    String getMdImgList(Long articleId);

    /**
     * 根据文章id获取文章信息
     *
     * @param articleId
     * @return
     */
    BlogArticle getArticle(Long articleId);

    /**
     * 修改文章信息
     *
     * @param request
     * @return
     */
    Boolean updateArticle(UpdateArticleRequest request);

    /**
     * 根据文章获取文章封面
     *
     * @param articleId
     * @return
     */
    String getArticleCoverById(Long articleId);

    /**
     * 公开或隐藏文章
     *
     * @param id
     * @param status
     * @return
     */
    Boolean toggleArticlePublic(Long id,Integer status);

    /**
     * 恢复文章
     *
     * @param id
     * @return
     */
    Boolean revertArticle(Long id);

    /**
     * 修改文章置顶信息
     *
     * @param id
     * @param isTop
     * @return
     */
    Boolean updateTop(Long id,Integer isTop);

    /**
     * 删除文章
     *
     * @param id
     * @param status
     * @return
     */
    Boolean deleteArticle(Long id,Integer status);

    /**
     *博客前台获取文章列表
     *
     * @param current 当前页
     * @param size 每页显示条数
     * @return 首页文章列表
     */
    PageInfoResult<BlogArticle> blogHomeGetArticleList(Integer current,Integer size);

    /**
     * 根据文章id获取推荐文章
     *
     * @param articleId 文章ID
     * @return  推荐文章列表
     */
    RecommendResult getRecommendArticleById(Long articleId);

    /**
     * 时间轴
     *
     * @param current 当前页
     * @param size 每页显示条数
     * @return 时间轴文章列表
     */
    PageInfoResult<ArticleResDto> blogTimelineGetArticleList(Integer current, Integer size);

    /**
     * 通过分类id获取文章列表
     *
     * @param current 当前页
     * @param size 每页显示条数
     * @param categoryId 分类id
     * @return 文章列表
     */
    PageInfoResult<BlogArticle> getArticleListByCategoryId(Integer current,Integer size,Integer categoryId);

    /**
     * 通过tagId 获取到文章列表
     *
     * @param current
     * @param size
     * @param tagId
     * @return
     */
    PageInfoResult<BlogArticle> getArticleListByTagId(Integer current,Integer size,Long tagId);

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
    Boolean articleLike(Long id);

    /**
     * 取消文章点赞
     *
     * @param id
     * @return
     */
    Boolean cancelArticleLike(Long id);

    /**
     * 文章增加阅读时长
     *
     * @param id
     * @param duration
     * @return
     */
    Boolean addReadingDuration(Long id,Integer duration);

}
