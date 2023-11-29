package com.xc.blogbackend.utils;

import com.xc.blogbackend.model.domain.BlogArticle;
import com.xc.blogbackend.model.domain.request.UpdateArticleRequest;

/**
 * 填充实体类属性
 *
 * @author 星尘
 */
public class PaddingUtils {
    public static BlogArticle mapToBlogArticle(UpdateArticleRequest updateArticleRequest) {
        BlogArticle blogArticle = new BlogArticle();
        UpdateArticleRequest.ArticleDate articleDate = updateArticleRequest.getArticle();

        blogArticle.setId(articleDate.getId());
        blogArticle.setArticle_title(articleDate.getArticle_title());
        blogArticle.setAuthor_id(articleDate.getAuthor_id());
        blogArticle.setCategory_id(articleDate.getCategory().getId()); // 可能需要进一步处理
        blogArticle.setArticle_content(articleDate.getArticle_content());
        blogArticle.setArticle_cover(articleDate.getArticle_cover());
        blogArticle.setIs_top(articleDate.getIs_top());
        blogArticle.setStatus(articleDate.getStatus());
        blogArticle.setType(articleDate.getType());
        blogArticle.setOrigin_url(articleDate.getOrigin_url());
//        blogArticle.setCreatedAt(articleDate.getCreatedAt());
//        blogArticle.setUpdatedAt(articleDate.getUpdatedAt());
        blogArticle.setView_times(articleDate.getView_times());
        blogArticle.setArticle_description(articleDate.getArticle_description());
        blogArticle.setThumbs_up_times(articleDate.getThumbs_up_times());
        blogArticle.setReading_duration(articleDate.getReading_duration());
        blogArticle.setArticle_order(articleDate.getArticle_order());

        // 其他属性的设置

        return blogArticle;
    }
}
