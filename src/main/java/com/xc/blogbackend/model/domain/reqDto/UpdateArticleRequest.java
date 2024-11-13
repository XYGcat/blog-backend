package com.xc.blogbackend.model.domain.reqDto;

import com.xc.blogbackend.model.domain.entity.BlogCategory;
import com.xc.blogbackend.model.domain.entity.BlogTag;
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

        private Long id;
        private String articleTitle;
        private Long authorId;
        private Long categoryId;
        private String articleContent;
        private String articleCover;
        private Integer isTop;
        private Integer status;
        private Integer type;
        private String originUrl;
        private String createdAt;
        private String updatedAt;
        private Integer viewTimes;
        private String articleDescription;
        private Integer thumbsUpTimes;
        private Double readingDuration;
        private Integer articleOrder;
        private List<String> mdImgList; //文章内容中的图片链接

    }
}
