package com.xc.blogbackend.model.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private String articleTitle;
    private Integer tagId;
    private Integer categoryId;
    private Integer isTop;
    private Integer status;
    private List<String> createTime = new ArrayList<>();
}
