package com.xc.blogbackend.controller;

import com.xc.blogbackend.ai.enums.AiModelEnum;
import com.xc.blogbackend.ai.factory.AiServiceFactory;
import com.xc.blogbackend.ai.model.spark.ChatRequest;
import com.xc.blogbackend.ai.service.AiService;
import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * AI接口
 *
 * @author xc
 */
@Api(tags = "AI接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiServiceFactory aiServiceFactory;

    @ApiOperation(value = "AI对话")
    @PostMapping("/chat")
    public ResponseBodyEmitter chatProcess(@RequestBody ChatRequest chatRequest) {
        AiService aiService = aiServiceFactory.getAiService("OPENAI");
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

    @ApiOperation(value = "获取大模型列表")
    @PostMapping("/modelList")
    public BaseResponse<List<String>> aiModelList(){
        return ResultUtils.success(AiModelEnum.getAllModel());
    }
}
