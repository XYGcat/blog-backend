package com.xc.blogbackend.ai.service.impl;

import com.xc.blogbackend.ai.client.OpenAiStreamClient;
import com.xc.blogbackend.ai.enums.AiErrorEnum;
import com.xc.blogbackend.ai.enums.AiModelEnum;
import com.xc.blogbackend.ai.listener.OpenAISSEEventSourceListener;
import com.xc.blogbackend.ai.model.openai.ChatCompletion;
import com.xc.blogbackend.ai.model.openai.Message;
import com.xc.blogbackend.ai.model.spark.ChatRequest;
import com.xc.blogbackend.ai.service.AiService;
import com.xc.blogbackend.exception.BusinessException;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * OpenAI 的实现，bean 名称为 "OPENAI"
 */
@Slf4j
@Service("OPENAI")
public class OpenAiServiceImpl implements AiService {

    private String apiKey;
    private String apiHost;
    private String model;

    private Dotenv dotenv;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 构造函数
     */
    public OpenAiServiceImpl() {
    }

    @Override
    public ResponseBodyEmitter chatProcess(ChatRequest chatRequest) {
        log.info("当前环为境：{}", env);
        String requestModel = chatRequest.getModel();
        if (ObjectUtils.isEmpty(requestModel)){
            throw new BusinessException(AiErrorEnum.MODEL_NOT_NULL);
        }
        if ("dev".equalsIgnoreCase(env)) {
            // 开发环境使用 dotenv 获取配置信息
            dotenv = Dotenv.configure().ignoreIfMissing().load();
            if (dotenv == null) {
                log.error("dotenv not found");
            }
            this.apiKey = dotenv.get(String.format("%s_API_KEY", AiModelEnum.getName(requestModel)));
        } else {
            // 生产环境从系统环境变量获取
            this.apiKey = System.getenv(String.format("%s_API_KEY", AiModelEnum.getName(requestModel)));
        }
        if (this.apiKey == null) {
            throw new BusinessException(AiErrorEnum.API_KEYS_NOT_NULL);
        }
        this.apiHost = AiModelEnum.getHost(requestModel);
        this.model = requestModel;

        // 创建 ResponseBodyEmitter 实例
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        // 异步执行聊天处理逻辑
        CompletableFuture.runAsync(() -> {
            // 封装 OpenAi 请求参数
            String prompt = chatRequest.getPrompt();
            Message message = Message.builder().role("user").content(prompt).build();
            ChatCompletion chatCompletion = ChatCompletion
                    .builder()
                    .model(model)
                    .temperature(0.2)
                    .maxTokens(2048)
                    .messages(Collections.singletonList(message))
                    .stream(true)
                    .build();

            OpenAiStreamClient client = OpenAiStreamClient.builder()
                    .apiKey(apiKey)
                    .apiHost(apiHost)
                    .build();

            // 使用匿名内部类创建 SSE 监听器，推送数据到 emitter
            client.streamChatCompletion(chatCompletion, new OpenAISSEEventSourceListener<>(emitter));
        });
        return emitter;
    }
}
