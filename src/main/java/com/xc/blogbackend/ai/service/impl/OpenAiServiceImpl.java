package com.xc.blogbackend.ai.service.impl;

import com.xc.blogbackend.ai.AiService;
import com.xc.blogbackend.ai.client.OpenAiStreamClient;
import com.xc.blogbackend.ai.enums.AiModelEnum;
import com.xc.blogbackend.ai.listener.OpenAISSEEventSourceListener;
import com.xc.blogbackend.ai.model.openai.ChatCompletion;
import com.xc.blogbackend.ai.model.openai.Message;
import com.xc.blogbackend.ai.model.spark.ChatRequest;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
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

    private final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    /**
     * 构造函数，从环境变量中获取 Open AI 的配置信息
     */
    public OpenAiServiceImpl() {

        this.apiKey = dotenv.get("OPENAI_API_KEY");
        this.apiHost = AiModelEnum.OPENAI_GPT_3_5_TURBO.getHost();
        this.model = AiModelEnum.OPENAI_GPT_3_5_TURBO.getModel();
    }

    @Override
    public ResponseBodyEmitter chatProcess(ChatRequest chatRequest, String... aiModel) {
        if (aiModel != null){
            this.apiKey = dotenv.get(String.format("%s_API_KEY", AiModelEnum.getName(aiModel[0])));
            this.apiHost = AiModelEnum.getHost(aiModel[0]);
            this.model = aiModel[0];
        }

        // 创建 ResponseBodyEmitter 实例
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        // 异步执行聊天处理逻辑
        CompletableFuture<Void> user = CompletableFuture.runAsync(() -> {
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
