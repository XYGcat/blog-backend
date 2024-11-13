package com.xc.blogbackend.model.domain.resDto;

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

    private Long id;
    private String articleContent;
    private String articleTitle;
}
