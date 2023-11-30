package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogTag;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogTagService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    /**
     * 条件分页获取标签
     *
     * @param request
     * @return
     */
    @PostMapping("/getTagList")
    public BaseResponse<PageInfoResult<BlogTag>> getTagList(@RequestBody Map<String,Object> request){
        Integer current = (Integer) request.get("current");
        Integer size = (Integer) request.get("size");
        String tag_name = (String) request.get("tag_name");
        PageInfoResult<BlogTag> tagList = blogTagService.getTalkList(current, size, tag_name);

        return ResultUtils.success(tagList,"分页查找标签成功");
    }

    /**
     * 修改标签
     *
     * @param request
     * @return
     */
    @PutMapping("/update")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> updateTag(@RequestBody Map<String,Object> request){
        Integer id = (Integer) request.get("id");
        String tag_name = (String) request.get("tag_name");
        Boolean aBoolean = blogTagService.updateTag(id, tag_name);
        return ResultUtils.success(aBoolean,"修改标签成功");
    }

    /**
     * 删除标签
     *
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> deleteTags(@RequestBody Map<String,List<Integer>> request){
        List<Integer> tagIdList = request.get("tagIdList");
        Boolean aBoolean = blogTagService.deleteTags(tagIdList);
        return ResultUtils.success(aBoolean,"删除标签成功");
    }

    /**
     * 新增标签
     *
     * @param request
     * @return
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<BlogTag> addTag(@RequestBody Map<String,Object> request){
        String tag_name = (String) request.get("tag_name");
        BlogTag tag = blogTagService.createTag(tag_name);
        return ResultUtils.success(tag,"新增标签成功");
    }
}
