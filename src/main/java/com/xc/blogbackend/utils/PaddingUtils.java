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

        String mdImgList = String.valueOf(updateArticleRequest.getMdImgList());

        blogArticle.setId(updateArticleRequest.getId());
        blogArticle.setArticleTitle(updateArticleRequest.getArticleTitle());
        blogArticle.setAuthorId(updateArticleRequest.getAuthorId());
        blogArticle.setCategoryId(updateArticleRequest.getCategory().getId()); // 可能需要进一步处理
        blogArticle.setArticleContent(updateArticleRequest.getArticleContent());
        blogArticle.setArticleCover(updateArticleRequest.getArticleCover());
        blogArticle.setIsTop(updateArticleRequest.getIsTop());
        blogArticle.setStatus(updateArticleRequest.getStatus());
        blogArticle.setType(updateArticleRequest.getType());
        blogArticle.setOriginUrl(updateArticleRequest.getOriginUrl());
//        blogArticle.setCreatedAt(updateArticleRequest.getCreatedAt());
//        blogArticle.setUpdatedAt(updateArticleRequest.getUpdatedAt());
        blogArticle.setViewTimes(updateArticleRequest.getViewTimes());
        blogArticle.setArticleDescription(updateArticleRequest.getArticleDescription());
        blogArticle.setThumbsUpTimes(updateArticleRequest.getThumbsUpTimes());
        blogArticle.setReadingDuration(updateArticleRequest.getReadingDuration());
        blogArticle.setArticleOrder(updateArticleRequest.getArticleOrder());
        blogArticle.setMdImgList(mdImgList);

        // 其他属性的设置

        return blogArticle;
    }
}
