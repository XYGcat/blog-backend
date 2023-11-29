package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.result.StatisticResult;
import com.xc.blogbackend.service.BlogArticleService;
import com.xc.blogbackend.service.BlogCategoryService;
import com.xc.blogbackend.service.BlogTagService;
import com.xc.blogbackend.service.BlogUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *首页接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private BlogArticleService blogArticleService;

    @Resource
    private BlogTagService blogTagService;

    @Resource
    private BlogCategoryService blogCategoryService;

    @Resource
    private BlogUserService blogUserService;


    @GetMapping("/statistic")
    public BaseResponse<StatisticResult> userStatistic(){
        long articleCount = blogArticleService.getArticleCount();
        Long tagCount = blogTagService.getTagCount();
        Long categoryCount = blogCategoryService.getCategoryCount();
        Long userCount = blogUserService.getUserCount();

        StatisticResult statisticResult = new StatisticResult();
        statisticResult.setArticleCount(articleCount);
        statisticResult.setTagCount(tagCount);
        statisticResult.setCategoryCount(categoryCount);
        statisticResult.setUserCount(userCount);
        return ResultUtils.success(statisticResult,"获取数据统计成功");
    }
}
