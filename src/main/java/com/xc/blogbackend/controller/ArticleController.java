package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogArticle;
import com.xc.blogbackend.model.domain.request.ArticleRequest;
import com.xc.blogbackend.model.domain.request.TitleExistRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogArticleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *文章接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private BlogArticleService blogArticleService;

    /**
     * 条件分页获取文章
     *
     * @return
     */
    @PostMapping("/getArticleList")
    public BaseResponse<PageInfoResult<BlogArticle>> getArticleList(@RequestBody ArticleRequest articleRequest){
        PageInfoResult<BlogArticle> articleList = blogArticleService.getArticleList(articleRequest);

        return ResultUtils.success(articleList);
    }


    @PostMapping("/titleExist")
    public BaseResponse<Boolean> getArticleInfoByTitle(@RequestBody TitleExistRequest titleExistRequest){
        String id = titleExistRequest.getId();
        String article_title = titleExistRequest.getArticle_title();
        Boolean articleInfoByTitle = blogArticleService.getArticleInfoByTitle(id, article_title);
        return ResultUtils.success(articleInfoByTitle);
    }
}
