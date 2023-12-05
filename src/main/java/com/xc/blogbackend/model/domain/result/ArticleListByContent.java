package com.xc.blogbackend.model.domain.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 前台全局搜索文章的返回体
 *
 * @author 星尘
 */
@Data
public class ArticleListByContent implements Serializable {
    private static final long serialVersionUID = 8354643003551073666L;

    private Integer id;
    private String article_content;
    private String article_title;
}
