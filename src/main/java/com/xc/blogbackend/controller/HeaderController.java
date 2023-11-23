package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogHeader;
import com.xc.blogbackend.service.BlogHeaderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 头部背景图接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/pageHeader")
public class HeaderController {

    @Resource
    private BlogHeaderService blogHeaderService;

    /**
     * 获取所有背景图
     *
     * @return
     */
    @GetMapping("/getAll")
    public BaseResponse<List<BlogHeader>> getAllHeader(){
        List<BlogHeader> allHeader = blogHeaderService.getAllHeader();
        return ResultUtils.success(allHeader,"获取所有背景成功");
    }
}
