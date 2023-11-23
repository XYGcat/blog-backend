package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogLinks;
import com.xc.blogbackend.model.domain.request.PageRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogLinksService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 友链接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/links")
public class LinksController {

    @Resource
    private BlogLinksService blogLinksService;

    @PostMapping("/getLinksList")
    public BaseResponse<PageInfoResult<BlogLinks>> getLinksList(@RequestBody PageRequest pageRequest){
        PageInfoResult<BlogLinks> linksList = blogLinksService.getLinksList(pageRequest);

        return ResultUtils.success(linksList,"查询友链成功");
    }
}
