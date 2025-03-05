package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.ai.ChatReqDto;
import com.xc.blogbackend.service.AiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "AI接口")
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private AiService aiService;

    @ApiOperation(value = "AI对话")
    @PostMapping("/chat")
    public ResponseBodyEmitter chatProcess(@RequestBody ChatReqDto chatRequest) {
        return aiService.chatProcess(chatRequest);
    }

    /**
     * 配置并存储大模型温度等属性
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "配置大模型属性")
    @PostMapping("/chat/config")
    public BaseResponse<Boolean> chatConfig(HttpServletRequest request){
        return ResultUtils.success(true);
    }
}
