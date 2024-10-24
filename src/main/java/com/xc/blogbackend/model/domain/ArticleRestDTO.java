package com.xc.blogbackend.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于传输创建文章相关信息的对象
 */
@Data
public class ArticleRestDTO implements Serializable {

    private String id; // 文章 ID
    private String articleTitle; // 文章标题
    private Integer authorId; // 作者 ID
    private String articleContent; // 文章内容
    private String articleCover; // 文章封面
    private Integer isTop; // 是否置顶，1 表示置顶，2 表示取消置顶
    private Integer articleOrder; // 置顶文章排序
    private Integer status; // 文章状态，1 表示公开，2 表示私密，3 表示回收站（相当于草稿）
    private Integer type; // 文章类型，1 表示原创，2 表示翻译，3 表示转载
    private String originUrl; // 原文链接，翻译或转载时填写
//    private List<String> coverList; // 封面列表
    private String articleDescription; // 文章描述

    private String categoryId; //分类id

    public void setValues(
            String id,
            String articleTitle,
            Integer authorId,
            String articleContent,
            String articleCover,
            Integer isTop,
            Integer articleOrder,
            Integer status,
            Integer type,
            String originUrl,
            String articleDescription
    ) {
        this.id = id;
        this.articleTitle = articleTitle;
        this.authorId = authorId;
        this.articleContent = articleContent;
        this.articleCover = articleCover;
        this.isTop = isTop;
        this.articleOrder = articleOrder;
        this.status = status;
        this.type = type;
        this.originUrl = originUrl;
        this.articleDescription = articleDescription;
    }
}
