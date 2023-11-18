package com.xc.blogbackend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询文章标题是否重复请求体
 *
 * @author 星尘
 */
@Data
public class TitleExistRequest implements Serializable {
    private static final long serialVersionUID = 9075383600717772790L;

    private String id;
    private String article_title;
}
