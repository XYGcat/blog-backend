package com.xc.blogbackend.model.domain.request;

import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.model.domain.BlogTag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 更新文章请求体
 *
 * @author 星尘
 */
@Data
public class UpdateArticleRequest implements Serializable {
    private static final long serialVersionUID = -7727618969592353305L;

    private ArticleDate article;

    @Data
    public static class ArticleDate{

        private String authorName;  //作者名称
        private BlogCategory category; // 文章所属的分类信息
        private String categoryName;    //分类名称
        private List<Map> coverList; // 封面列表
        private List<BlogTag> tagList; // 标签列表
        private List<String> tagNameList;

        private Integer id;
        private String article_title;
        private Integer author_id;
        private Integer category_id;
        private String article_content;
        private String article_cover;
        private Integer is_top;
        private Integer status;
        private Integer type;
        private String origin_url;
        private String createdAt;
        private String updatedAt;
        private Integer view_times;
        private String article_description;
        private Integer thumbs_up_times;
        private Double reading_duration;
        private Integer article_order;
        private List<String> mdImgList; //文章内容中的图片链接

    }
}
