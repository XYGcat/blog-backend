package com.xc.blogbackend.service;

import com.xc.blogbackend.model.domain.ai.ChatReqDto;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * AI对话服务
 *
 * @author xc
 */
public interface AiService {

    /**
     * AI对话
     *
     * @param chatRequest
     * @return
     */
    ResponseBodyEmitter chatProcess(ChatReqDto chatRequest);
}
