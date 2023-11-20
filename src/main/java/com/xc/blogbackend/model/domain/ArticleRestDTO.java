package com.xc.blogbackend.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于传输创建文章相关信息的对象
 */
@Data
public class ArticleRestDTO implements Serializable {

    private String id; // 文章 ID
    private String article_title; // 文章标题
    private Integer author_id; // 作者 ID
    private String article_content; // 文章内容
    private String article_cover; // 文章封面
    private Integer is_top; // 是否置顶，1 表示置顶，2 表示取消置顶
    private Integer article_order; // 置顶文章排序
    private Integer status; // 文章状态，1 表示公开，2 表示私密，3 表示回收站（相当于草稿）
    private Integer type; // 文章类型，1 表示原创，2 表示翻译，3 表示转载
    private String origin_url; // 原文链接，翻译或转载时填写
//    private List<String> coverList; // 封面列表
    private String article_description; // 文章描述
    private String category_id; //分类id

    public void setValues(
            String id,
            String article_title,
            Integer author_id,
            String article_content,
            String article_cover,
            Integer is_top,
            Integer article_order,
            Integer status,
            Integer type,
            String origin_url,
            String article_description
    ) {
        this.id = id;
        this.article_title = article_title;
        this.author_id = author_id;
        this.article_content = article_content;
        this.article_cover = article_cover;
        this.is_top = is_top;
        this.article_order = article_order;
        this.status = status;
        this.type = type;
        this.origin_url = origin_url;
        this.article_description = article_description;
    }
}
