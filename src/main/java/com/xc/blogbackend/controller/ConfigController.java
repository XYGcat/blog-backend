package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.BlogConfig;
import com.xc.blogbackend.service.BlogConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 网站设置接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private BlogConfigService blogConfigService;

    /**
     *获取网站设置
     *
     * @return
     */
    @GetMapping("/getdata")
    public BaseResponse<BlogConfig> getConfig(){
        BlogConfig config = blogConfigService.getConfig();
        if (config != null) {
            return ResultUtils.success(config,"获取网站设置成功");
        }else {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
    }

    /**
     * 增加网站访问次数
     *
     * @return
     */
    @PutMapping("/addView")
    public BaseResponse<String> addView(){
        String res = blogConfigService.addView();
        if (res == "添加成功") {
            return ResultUtils.success(res,"增加访问量成功");
        }else {
            return ResultUtils.success(res,"请先初始化网站信息");
        }
    }
}
