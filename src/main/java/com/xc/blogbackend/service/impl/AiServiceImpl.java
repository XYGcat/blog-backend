package com.xc.blogbackend.service.impl;

import com.xc.blogbackend.client.AiWebSocketClient;
import com.xc.blogbackend.handler.AiChatRequestHandler;
import com.xc.blogbackend.listener.AiListener;
import com.xc.blogbackend.model.domain.ai.AiReqDto;
import com.xc.blogbackend.model.domain.ai.AiResDto;
import com.xc.blogbackend.model.domain.ai.ChatReqDto;
import com.xc.blogbackend.service.AiService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * AI接口实现
 */
@Slf4j
@Service
public class AiServiceImpl implements AiService {

    private final String model = "SPARK";
    private final Map<String, Map<String, String>> modelConfigs = new HashMap<>();


    // 构造函数中动态加载所有模型的 API Key
    public AiServiceImpl() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // 为不同的模型初始化配置
        Map<String, String> sparkConfig = new HashMap<>();
        sparkConfig.put("SPARK_APP_ID", dotenv.get("SPARK_APP_ID"));
        sparkConfig.put("SPARK_APP_KEY", dotenv.get("SPARK_APP_KEY"));
        sparkConfig.put("SPARK_API_SECRET", dotenv.get("SPARK_API_SECRET"));
        sparkConfig.put("SPARK_API_HOST_WSS_V3_5", dotenv.get("SPARK_API_HOST_WSS_V3_5"));

        Map<String, String> openAiConfig = new HashMap<>();
        openAiConfig.put("OPENAI_API_BASE", dotenv.get("OPENAI_API_BASE"));
        openAiConfig.put("OPENAI_API_KEY", dotenv.get("OPENAI_API_KEY"));
        openAiConfig.put("OPENAI_API_VERSION", dotenv.get("OPENAI_API_VERSION"));
        openAiConfig.put("OPENAI_API_MODEL", dotenv.get("OPENAI_API_MODEL"));

        // 将每个模型的配置放入 Map 中
        modelConfigs.put("SPARK", sparkConfig);
        modelConfigs.put("OPENAI", openAiConfig);
    }

    @Override
    public ResponseBodyEmitter chatProcess(ChatReqDto chatRequest) {
        Map<String, String> modelConfig = modelConfigs.get(model);
        String appId = modelConfig.get("SPARK_APP_ID");
        String apiKey = modelConfig.get("SPARK_APP_KEY");
        String apiSecret = modelConfig.get("SPARK_API_SECRET");
        String apiHost = modelConfig.get("SPARK_API_HOST_WSS_V3_5");

        // 创建 ResponseBodyEmitter 实例
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        // 异步执行聊天处理逻辑
        CompletableFuture.runAsync(() -> {
            String prompt = chatRequest.getPrompt();

            // 封装 Spark 请求参数
            AiChatRequestHandler aiChatRequestHandler = new AiChatRequestHandler();
            AiReqDto aiReqDto = aiChatRequestHandler.handle(prompt, appId);

            // 创建 SparkDeskClient 实例用于与 Spark 模型通信
            AiWebSocketClient aiWebSocketClient = AiWebSocketClient.builder()
                    .host(apiHost)
                    .appid(appId)
                    .apiKey(apiKey)
                    .apiSecret(apiSecret)
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
