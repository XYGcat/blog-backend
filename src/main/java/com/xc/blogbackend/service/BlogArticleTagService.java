package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogArticleTag;

import java.util.List;
import java.util.Map;

/**
* @author XC
* @description 针对表【blog_article_tag】的数据库操作Service
* @createDate 2023-11-16 16:08:15
*/
public interface BlogArticleTagService extends IService<BlogArticleTag> {

    /**
     * 根据标签id获取该标签下所有的文章id
     *
     * @return
     */
    List<Integer> getArticleIdListByTagId(int tag_id);

    /**
     * 根据文章id获取标签名称列表
     *
     * @param article_id
     * @return
     */
    Map<String, Object> getTagListByArticleId(int article_id);
}
