package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogTalk;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogTalkService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
