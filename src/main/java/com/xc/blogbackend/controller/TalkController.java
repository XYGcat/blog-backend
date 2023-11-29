package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogTalk;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogTalkService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *说说接口
 *
 * @author 星尘
 */
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
    @PutMapping("/updateTalk")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
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
    @PostMapping("/publishTalk")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Map<String,Integer>> publishTalk(@RequestBody BlogTalk blogTalk){
        BlogTalk publishTalk = blogTalkService.publishTalk(blogTalk);
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("id",publishTalk.getId());
        return ResultUtils.success(hashMap,"发布说说成功");
    }
}
