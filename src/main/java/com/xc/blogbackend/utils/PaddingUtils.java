package com.xc.blogbackend.utils;

import com.xc.blogbackend.model.domain.entity.BlogArticle;
import com.xc.blogbackend.model.domain.reqDto.UpdateArticleRequest;

/**
 * 填充实体类属性
 *
 * @author 星尘
 */
public class PaddingUtils {
    public static BlogArticle mapToBlogArticle(UpdateArticleRequest updateArticleRequest) {
        BlogArticle blogArticle = new BlogArticle();
        UpdateArticleRequest.ArticleDate articleDate = updateArticleRequest.getArticle();

        String mdImgList = String.valueOf(articleDate.getMdImgList());

        blogArticle.setId(articleDate.getId());
        blogArticle.setArticleTitle(articleDate.getArticleTitle());
        blogArticle.setAuthorId(articleDate.getAuthorId());
        blogArticle.setCategoryId(articleDate.getCategory().getId()); // 可能需要进一步处理
        blogArticle.setArticleContent(articleDate.getArticleContent());
        blogArticle.setArticleCover(articleDate.getArticleCover());
        blogArticle.setIsTop(articleDate.getIsTop());
        blogArticle.setStatus(articleDate.getStatus());
        blogArticle.setType(articleDate.getType());
        blogArticle.setOriginUrl(articleDate.getOriginUrl());
//        blogArticle.setCreatedAt(articleDate.getCreatedAt());
//        blogArticle.setUpdatedAt(articleDate.getUpdatedAt());
        blogArticle.setViewTimes(articleDate.getViewTimes());
        blogArticle.setArticleDescription(articleDate.getArticleDescription());
        blogArticle.setThumbsUpTimes(articleDate.getThumbsUpTimes());
        blogArticle.setReadingDuration(articleDate.getReadingDuration());
        blogArticle.setArticleOrder(articleDate.getArticleOrder());
        blogArticle.setMdImgList(mdImgList);

        // 其他属性的设置

        return blogArticle;
    }
}
