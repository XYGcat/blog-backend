package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogHeader;
import com.xc.blogbackend.service.BlogHeaderService;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 头部背景图接口
 *
 * @author 星尘
 */
@Api(tags = "头部背景图接口")
@RestController
@RequestMapping("/pageHeader")
public class HeaderController {

    @Resource
    private BlogHeaderService blogHeaderService;

    @Resource
    private Qiniu qiniu;

    /**
     * 获取所有背景图
     *
     * @return
     */
    @ApiOperation(value = "获取所有背景图")
    @GetMapping("/getAll")
    public BaseResponse<List<BlogHeader>> getAllHeader(){
        List<BlogHeader> allHeader = blogHeaderService.getAllHeader();
        return ResultUtils.success(allHeader,"获取所有背景成功");
    }

    /**
     * 根据是否有id来判断新增/编辑背景
     *
     * @param blogHeader
     * @return
     */
    @ApiOperation(value = "根据是否有id来判断新增/编辑背景")
    @PostMapping("/addOrUpdate")
    public BaseResponse<Boolean> addOrUpdateHeader(@RequestBody BlogHeader blogHeader){
        String msg;
        Integer id = blogHeader.getId();
        String route_name = blogHeader.getRoute_name();

        BlogHeader oneByPath = blogHeaderService.getOneByPath(route_name);
        if (id != null){
            if (oneByPath != null && oneByPath.getId() != id){
                return ResultUtils.error(ErrorCode.PARAMS_ERROR,"已经存在相同的背景路径");
            }
            msg = "修改";
        }else {
            if (oneByPath != null){
                return ResultUtils.error(ErrorCode.PARAMS_ERROR,"已经存在相同的背景路径");
            }
            msg = "新增";
        }

        Boolean aBoolean = blogHeaderService.addOrUpdateHeader(blogHeader);

        return ResultUtils.success(aBoolean,msg + "背景成功");
    }

    /**
     * 删除背景
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "删除背景")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteHeader(@RequestBody Map<String,Object> request){
        Integer id = (Integer) request.get("id");
        String url = (String) request.get("url");
        String subString = StringManipulation.subString(url);

        Boolean aBoolean = blogHeaderService.deleteHeader(id);
        if (subString != null) {
            Boolean deleteFile = qiniu.deleteFile(subString);
        }

        return ResultUtils.success(aBoolean,"删除背景成功");
    }
}
