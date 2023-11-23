package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogMessage;
import com.xc.blogbackend.model.domain.request.PageRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogMessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 留言服务接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private BlogMessageService blogMessageService;

    /**
     * 分页获取留言
     *
     * @param request
     * @return
     */
    @PostMapping("/getMessageList")
    public BaseResponse<PageInfoResult<BlogMessage>> getMessageList(@RequestBody PageRequest request){
        Integer current = request.getCurrent();
        Integer size = request.getSize();
        String message = request.getMessage();
        List<String> time = request.getTime();

        PageInfoResult<BlogMessage> messageList = blogMessageService.getMessageList(current, size, message, time);

        return ResultUtils.success(messageList,"分页获取留言成功");
    }
}
