package com.xc.blogbackend.ai.service.impl;

import com.xc.blogbackend.ai.AiService;
import com.xc.blogbackend.ai.client.OpenAiWebSocketClient;
import com.xc.blogbackend.ai.enums.AiModelEnum;
import com.xc.blogbackend.ai.listener.OpenAiListener;
import com.xc.blogbackend.ai.model.openai.OpenAiRequest;
import com.xc.blogbackend.ai.model.openai.OpenAiResponse;
import com.xc.blogbackend.ai.model.spark.ChatRequest;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Arrays;
import java.util.List;
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
     * 构造函数，从环境变量中获取 Spark AI 的配置信息
     */
    public OpenAiServiceImpl() {

        this.apiKey = dotenv.get("OPENAI_API_KEY");
        this.apiHost = AiModelEnum.OPENAI.getHost();
        this.model = AiModelEnum.OPENAI.getModel();
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
        CompletableFuture.runAsync(() -> {
            String prompt = chatRequest.getPrompt();

            // 封装 OpenAi 请求参数
            // 创建消息对象
            OpenAiRequest.Message message = new OpenAiRequest.Message("user", prompt);

            // 创建流选项对象
            OpenAiRequest.StreamOptions streamOptions = new OpenAiRequest.StreamOptions(true);

            // 构建 OpenAiRequest 实例
            OpenAiRequest request = new OpenAiRequest(
                    aiModel[0],                           // 模型
                    Arrays.asList(message),                  // 消息列表
                    true,                                    // 是否流式
                    streamOptions                            // 流式配置
            );

            // 创建 SparkDeskClient 实例用于与 Spark 模型通信
            OpenAiWebSocketClient aiWebSocketClient = OpenAiWebSocketClient.builder()
                    .host(apiHost)
                    .apiKey(apiKey)
                    .build();

            // 发起聊天请求并设置回调
            aiWebSocketClient.chat(new OpenAiListener(request) {
                @Override
                public void onChatOutput(OpenAiResponse openAIResponse) {
                    try {
                        String content = openAIResponse.getChoices().get(0).getDelta().getContent();
                        log.info("content: {}", content);
                        // 将响应内容发送给客户端
                        emitter.send(content);
                    } catch (Exception e) {
                        // 发生错误时完成响应并抛出异常
                        emitter.completeWithError(e);
                    }
                }
                @Override
                public void onChatEnd(List<OpenAiResponse> openAIResponse) {
                    emitter.complete();
                }
            });
        });

        return emitter;
    }

}
