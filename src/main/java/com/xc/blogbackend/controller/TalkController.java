package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogTalk;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogTalkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *说说接口
 *
 * @author 星尘
 */
@Api(tags = "说说接口")
@RestController
@RequestMapping("/talk")
public class TalkController {

    @Resource
    private BlogTalkService blogTalkService;

    /**
     * 分页获取说说
     *
     * @param requestData
     * @return
     */
    @ApiOperation(value = "分页获取说说")
    @PostMapping("/getTalkList")
    public BaseResponse<PageInfoResult<BlogTalk>> getTalkList(@RequestBody Map<String,Integer> requestData){
        Integer current = requestData.get("current");
        Integer size = requestData.get("size");
        Integer status = requestData.get("status");
        PageInfoResult<BlogTalk> talkList = blogTalkService.getTalkList(current, size, status);

        return ResultUtils.success(talkList);
    }

    /**
     * 根据id 获取说说详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id 获取说说详情")
    @GetMapping("/getTalkById/{id}")
    public BaseResponse<BlogTalk> getTalkById(@PathVariable Integer id){
        BlogTalk talkById = blogTalkService.getTalkById(id);
        return ResultUtils.success(talkById,"获取说说详情成功");
    }

    /**
     * 修改说说
     *
     * @param blogTalk
     * @return
     */
    @ApiOperation(value = "修改说说")
    @PutMapping("/updateTalk")
    public BaseResponse<Boolean> updateTalk(@RequestBody BlogTalk blogTalk){
        Boolean aBoolean = blogTalkService.updateTalk(blogTalk);
        return ResultUtils.success(aBoolean,"修改说说成功");
    }

    /**
     * 修改说说公开置顶状态 1 置顶 2 不置顶
     *
     * @param id
     * @param is_top
     * @return
     */
    @ApiOperation(value = "修改说说公开置顶状态")
    @PutMapping("/toggleTop/{id}/{is_top}")
    public BaseResponse<Boolean> toggleTop(@PathVariable Integer id,@PathVariable Integer is_top){
        String message;
        if (is_top == 1) {
            message = "置顶";
        }else {
            message = "取消置顶";
        }
        Boolean aBoolean = blogTalkService.toggleTop(id, is_top);
        return ResultUtils.success(aBoolean,message + "说说成功");
    }

    /**
     * 修改说说公开私密状态 1 公开 2 私密
     *
     * @param id
     * @param status
     * @return
     */
    @ApiOperation(value = "修改说说公开私密状态")
    @PutMapping("/togglePublic/{id}/{status}")
    public BaseResponse<Boolean> togglePublic(@PathVariable Integer id,@PathVariable Integer status){
        String message;
        if (status == 1) {
            message = "公开";
        }else {
            message = "私密";
        }
        Boolean aBoolean = blogTalkService.togglePublic(id, status);
        return ResultUtils.success(aBoolean,message + "说说成功");
    }

    /**
     * 删除说说
     *
     * @param id
     * @param status
     * @return
     */
    @ApiOperation(value = "删除说说")
    @DeleteMapping("/deleteTalkById/{id}/{status}")
    public BaseResponse<Boolean> deleteTalkById(@PathVariable Integer id,@PathVariable Integer status){
        String message;
        if (status == 3) {
            message = "删除";
        }else {
            message = "回收";
        }
        Boolean aBoolean = blogTalkService.deleteTalkById(id, status);
        return ResultUtils.success(aBoolean,message + "说说成功");
    }

    /**
     * 恢复说说
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "恢复说说")
    @PutMapping("/revertTalk/{id}")
    public BaseResponse<Boolean> revertTalk(@PathVariable Integer id){
        Boolean aBoolean = blogTalkService.revertTalk(id);
        return ResultUtils.success(aBoolean,"恢复说说成功");
    }


    /**
     * 发布说说
     *
     * @param blogTalk
     * @return
     */
    @ApiOperation(value = "发布说说")
    @PostMapping("/publishTalk")
    public BaseResponse<Map<String,Integer>> publishTalk(@RequestBody BlogTalk blogTalk){
        BlogTalk publishTalk = blogTalkService.publishTalk(blogTalk);
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("id",publishTalk.getId());
        return ResultUtils.success(hashMap,"发布说说成功");
    }

    /**
     * 前台
     * 获取说说列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "前台 获取说说列表")
    @PostMapping("/blogGetTalkList")
    public BaseResponse<PageInfoResult<BlogTalk>> blogGetTalkList(@RequestBody Map<String,Integer> request){
        Integer current = request.get("current");
        Integer size = request.get("size");
        Integer user_id = request.get("user_id");
        PageInfoResult<BlogTalk> talkPageInfoResult = blogTalkService.blogGetTalkList(current, size, user_id);
        return ResultUtils.success(talkPageInfoResult,"获取说说列表成功");
    }

    /**
     * 说说点赞
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "说说点赞")
    @PutMapping("/like/{id}")
    public BaseResponse<Boolean> talkLike(@PathVariable Integer id){
        Boolean aBoolean = blogTalkService.talkLike(id);
        return ResultUtils.success(aBoolean,"点赞成功");
    }

    /**
     * 取消点赞
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "取消点赞")
    @PutMapping("/cancelLike/{id}")
    public BaseResponse<Boolean> cancelTalkLike(@PathVariable Integer id){
        Boolean aBoolean = blogTalkService.cancelTalkLike(id);
        return ResultUtils.success(aBoolean,"取消点赞成功");
    }
}
