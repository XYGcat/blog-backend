package com.xc.blogbackend.model.domain.resDto;

import com.xc.blogbackend.model.domain.entity.BlogArticle;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐文章的返回体
 *
 * @author 星尘
 */
@Data
public class RecommendResult implements Serializable {
    private static final long serialVersionUID = -6218067293843741623L;

    private BlogArticle previous;
    private BlogArticle next;
    private List<BlogArticle> recommend;

}
