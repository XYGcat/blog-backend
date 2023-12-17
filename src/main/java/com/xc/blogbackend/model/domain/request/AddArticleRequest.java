package com.xc.blogbackend.model.domain.request;

import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.model.domain.BlogTag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 添加文章请求体
 *
 * @author 星尘
 */
@Data
public class AddArticleRequest implements Serializable {
    private static final long serialVersionUID = -7600022127166394703L;

    private ArticleDate finalArticle;

    @Data
    public static class ArticleDate{
        private Integer id; // 文章 ID
        private String article_title; // 文章标题
        private BlogCategory category; // 文章所属的分类信息
        private List<BlogTag> tagList; // 标签列表
        private Integer author_id; // 作者 ID
        private String article_content; // 文章内容
        private String article_cover; // 文章封面
        private Integer is_top; // 是否置顶，1 表示置顶，2 表示取消置顶
        private Integer article_order; // 置顶文章排序
        private Integer status; // 文章状态，1 表示公开，2 表示私密，3 表示回收站（相当于草稿）
        private Integer type; // 文章类型，1 表示原创，2 表示翻译，3 表示转载
        private String origin_url; // 原文链接，翻译或转载时填写
        private List<Map> coverList; // 封面列表
        private String article_description; // 文章描述
        private List<String> mdImgList; //文章内容中的图片链接
    }

}
