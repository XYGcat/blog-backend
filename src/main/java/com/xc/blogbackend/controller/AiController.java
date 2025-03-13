package com.xc.blogbackend.controller;

import com.xc.blogbackend.ai.AiService;
import com.xc.blogbackend.ai.factory.AiServiceFactory;
import com.xc.blogbackend.ai.model.spark.ChatRequest;
import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "AI接口")
@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiServiceFactory aiServiceFactory;

    @Autowired
    public AiController(AiServiceFactory aiServiceFactory) {
        this.aiServiceFactory = aiServiceFactory;
    }

    @ApiOperation(value = "AI对话")
    @PostMapping("/chat")
    public ResponseBodyEmitter chatProcess(@RequestBody ChatRequest chatRequest) {
        AiService aiService = aiServiceFactory.getAiService("SPARKAI");
        return aiService.chatProcess(chatRequest, "generalv3.5");
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
