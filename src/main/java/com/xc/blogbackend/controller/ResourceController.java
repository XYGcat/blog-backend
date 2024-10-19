package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BgResource;
import com.xc.blogbackend.service.BgResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资源导航接口
 *
 * @author xc
 */
@Api(tags = "资源导航接口")
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Resource
    private BgResourceService bgResourceService;

    /**
     * 根据分类获取站点
     * @param categoryId
     */
    @ApiOperation(value = "根据分类获取站点")
    @GetMapping("/sites/{categoryId}")
    public BaseResponse<List<BgResource>> getSiteToCategory(@PathVariable Integer categoryId) {
        List<BgResource> bgResourceList = bgResourceService.getSiteToCategory(categoryId);
        return ResultUtils.success(bgResourceList);
    }

    /**
     * 添加站点
     * @param bgResource
     */
    @ApiOperation(value = "添加站点")
    @PostMapping("/sites")
    public BaseResponse<Boolean> addSiteToCategory(@RequestBody BgResource bgResource){
        Boolean isAdd = bgResourceService.addSiteToCategory(bgResource);
        return ResultUtils.success(isAdd);
    }
}
