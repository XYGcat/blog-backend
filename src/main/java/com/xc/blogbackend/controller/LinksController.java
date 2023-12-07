package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogLinks;
import com.xc.blogbackend.model.domain.BlogNotify;
import com.xc.blogbackend.model.domain.request.PageRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogLinksService;
import com.xc.blogbackend.service.BlogNotifyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Resource
    private BlogNotifyService blogNotifyService;

    /**
     * 分页获取友链
     *
     * @param pageRequest
     * @return
     */
    @PostMapping("/getLinksList")
    public BaseResponse<PageInfoResult<BlogLinks>> getLinksList(@RequestBody PageRequest pageRequest){
        PageInfoResult<BlogLinks> linksList = blogLinksService.getLinksList(pageRequest);

        return ResultUtils.success(linksList,"查询友链成功");
    }

    /**
     * 新增/修改友链
     *
     * @param request
     * @return
     */
    @PostMapping("/addOrUpdate")
    public BaseResponse<Boolean> addOrUpdateLinks(@RequestBody Map<String,Object> request){
        Integer id = (Integer) request.get("id");
        String site_name = (String) request.get("site_name");
        String msg;

        Boolean aBoolean = blogLinksService.addOrUpdateLinks(request);

        if(id == null){
            BlogNotify blogNotify = blogNotifyService.addNotify
                    (1, 4, null, "您的收到了来自于：" + site_name + "的友链申请，点我去后台审核！");
        }

        if(id != null){
            msg = "修改";
        }else {
            msg = "发布";
        }

        return ResultUtils.success(aBoolean,msg + "友链成功");
    }

    /**
     * 批量审核友链
     *
     * @param request
     * @return
     */
    @PutMapping("/approve")
    public BaseResponse<Boolean> approveLinks(@RequestBody Map<String, List<Integer>> request){
        List<Integer> idList = request.get("idList");
        Boolean aBoolean = blogLinksService.approveLinks(idList);

        return ResultUtils.success(aBoolean,"审核友链成功");
    }

    /**
     * 批量删除友链
     *
     * @param request
     * @return
     */
    @PutMapping("/delete")
    public BaseResponse<Boolean> deleteLinks(@RequestBody Map<String, List<Integer>> request){
        List<Integer> idList = request.get("idList");
        Boolean aBoolean = blogLinksService.deleteLinks(idList);

        return ResultUtils.success(aBoolean,"删除友链成功");
    }
}
