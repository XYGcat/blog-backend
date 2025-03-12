package com.xc.blogbackend.ai.service.impl;

import com.xc.blogbackend.ai.AiService;
import com.xc.blogbackend.ai.client.AiWebSocketClient;
import com.xc.blogbackend.ai.enums.AiModelEnum;
import com.xc.blogbackend.ai.handler.AiChatRequestHandler;
import com.xc.blogbackend.ai.listener.AiListener;
import com.xc.blogbackend.ai.model.AiReqDto;
import com.xc.blogbackend.ai.model.AiResDto;
import com.xc.blogbackend.ai.model.ChatReqDto;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Spark AI 的实现，bean 名称为 "SPARKAI"
 */
@Slf4j
@Service("SPARKAI")
public class SparkServiceImpl implements AiService {

    private final String appId;
    private final String apiKey;
    private final String apiSecret;
    private final String apiHost;
    private final String model;


    /**
     * 构造函数，从环境变量中获取 Spark AI 的配置信息
     */
    public SparkServiceImpl() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        this.appId = dotenv.get("SPARK_APP_ID");
        this.apiKey = dotenv.get("SPARK_API_KEY");
        this.apiSecret = dotenv.get("SPARK_API_SECRET");
        this.apiHost = AiModelEnum.SPARK_MAX.getHost();
        this.model = AiModelEnum.SPARK_MAX.getModel();
    }

    @Override
    public ResponseBodyEmitter chatProcess(ChatReqDto chatRequest) {
        // 创建 ResponseBodyEmitter 实例
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        // 异步执行聊天处理逻辑
        CompletableFuture.runAsync(() -> {
            String prompt = chatRequest.getPrompt();

            // 封装 Spark 请求参数
            AiChatRequestHandler aiChatRequestHandler = new AiChatRequestHandler();
            AiReqDto aiReqDto = aiChatRequestHandler.handle(prompt, appId, model);

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

    @Override
    public String callAi(String prompt) {
        return "";
    }
}
