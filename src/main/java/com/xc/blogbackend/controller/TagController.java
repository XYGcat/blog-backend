package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogTag;
import com.xc.blogbackend.service.BlogTagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *标签接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private BlogTagService blogTagService;

    /**
     * 获取标签字典
     *
     * @return
     */
    @GetMapping("/getTagDictionary")
    public BaseResponse<List<BlogTag>> getTagDictionary(){
        List<BlogTag> tagDictionary = blogTagService.getTagDictionary();
        return ResultUtils.success(tagDictionary);
    }

}
