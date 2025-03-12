package com.xc.blogbackend.ai.service.impl;

import com.xc.blogbackend.ai.AiService;
import com.xc.blogbackend.ai.model.ChatReqDto;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * OpenAI 的实现，bean 名称为 "OPENAI"
 */
@Service("OPENAI")
public class OpenAiServiceImpl implements AiService {

    @Override
    public ResponseBodyEmitter chatProcess(ChatReqDto chatRequest) {
        return null;
    }

    @Override
    public String callAi(String prompt) {
        // 此处填入调用 OpenAI API 的逻辑
        return "OpenAI: response to [" + prompt + "]";
    }
}
