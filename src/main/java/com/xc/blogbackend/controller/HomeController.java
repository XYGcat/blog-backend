package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.result.StatisticResult;
import com.xc.blogbackend.service.BlogArticleService;
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


    @GetMapping("/statistic")
    public BaseResponse<StatisticResult> userStatistic(){
        long articleCount = blogArticleService.getArticleCount();
        StatisticResult statisticResult = new StatisticResult();
        statisticResult.setArticleCount(articleCount);
        return ResultUtils.success(statisticResult);
    }
}
