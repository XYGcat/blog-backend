package com.xc.blogbackend.service.impl;

import com.xc.blogbackend.client.AiWebSocketClient;
import com.xc.blogbackend.constant.AiConstant;
import com.xc.blogbackend.handler.AiChatRequestHandler;
import com.xc.blogbackend.listener.AiListener;
import com.xc.blogbackend.model.domain.ai.AiReqDto;
import com.xc.blogbackend.model.domain.ai.AiResDto;
import com.xc.blogbackend.model.domain.ai.ChatReqDto;
import com.xc.blogbackend.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * AI接口实现
 */
@Slf4j
@Service
public class AiServiceImpl implements AiService {

    @Override
    public ResponseBodyEmitter chatProcess(ChatReqDto chatRequest) {
        // 创建 ResponseBodyEmitter 实例
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        // 异步执行聊天处理逻辑
        CompletableFuture.runAsync(() -> {
            String prompt = chatRequest.getPrompt();

            // 封装 Spark 请求参数
            AiChatRequestHandler aiChatRequestHandler = new AiChatRequestHandler();
            AiReqDto aiReqDto = aiChatRequestHandler.handle(prompt);

            // 创建 SparkDeskClient 实例用于与 Spark 模型通信
            AiWebSocketClient aiWebSocketClient = AiWebSocketClient.builder()
                    .host(AiConstant.SPARK_API_HOST_WSS_V3_5)
                    .appid(AiConstant.APP_ID)
                    .apiKey(AiConstant.APP_KEY)
                    .apiSecret(AiConstant.API_SECRET)
                    .build();

            // 发起聊天请求并设置回调
            aiWebSocketClient.chat(new AiListener(aiReqDto) {
                @Override
                public void onChatOutput(AiResDto aiResDto) {
                    try {
                        String content = aiResDto.getPayload().getChoices().getText().get(0).getContent();
                        log.info("content: {}", content);
                        // 将响应内容发送给客户端
                        emitter.send(content);
                    } catch (Exception e) {
                        // 发生错误时完成响应并抛出异常
                        emitter.completeWithError(e);
                    }
                }
                @Override
                public void onChatEnd(List<AiResDto> aiResDtoList) {
                    emitter.complete();
                }
            });
        });

        return emitter;
    }
}
