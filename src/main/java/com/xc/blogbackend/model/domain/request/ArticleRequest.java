package com.xc.blogbackend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 条件分页获取文章请求体 getArticleList()
 *
 * @author 星尘
 */
@Data
public class ArticleRequest implements Serializable {
    private static final long serialVersionUID = -5474329482580023826L;

    private int current = 1;
    private int size = 10;
    private String article_title;
    private Integer tag_id;
    private Integer category_id;
    private Integer is_top;
    private Integer status;
    private String create_time;
}
