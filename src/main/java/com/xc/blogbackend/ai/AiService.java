package com.xc.blogbackend.ai;

import com.xc.blogbackend.ai.model.spark.ChatRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * AI对话服务
 *
 * @author xc
 */
public interface AiService {

    /**
     * 根据输入的提示返回 AI 的响应
     *
     * @param chatRequest 用户输入
     * @return AI 返回的响应文本
     */
    ResponseBodyEmitter chatProcess(ChatRequest chatRequest);
}
