package com.xc.blogbackend.ai.service.impl;

import com.xc.blogbackend.ai.client.SparkAiSocketClient;
import com.xc.blogbackend.ai.enums.AiModelEnum;
import com.xc.blogbackend.ai.handler.AiChatRequestHandler;
import com.xc.blogbackend.ai.listener.SparkListener;
import com.xc.blogbackend.ai.model.spark.ChatRequest;
import com.xc.blogbackend.ai.model.spark.SparkRequest;
import com.xc.blogbackend.ai.model.spark.SparkResponse;
import com.xc.blogbackend.ai.service.AiService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.profiles.active}")
    private String env;

    private String appId;
    private String apiKey;
    private String apiSecret;
    private String apiHost;
    private String model;

    /**
     * 构造函数，从环境变量中获取 Spark AI 的配置信息
     */
    public SparkServiceImpl() {
    }

    @Override
    public ResponseBodyEmitter chatProcess(ChatRequest chatRequest) {
        log.info("当前环为境：{}", env);
        if ("dev".equals(env)) {
            // 在开发环境中使用 dotenv
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            if (dotenv == null) {
                log.error("dotenv is null");
            }
            this.appId = dotenv.get("SPARK_APP_ID");
            this.apiKey = dotenv.get("SPARK_API_KEY");
            this.apiSecret = dotenv.get("SPARK_API_SECRET");
        } else {
            // 在生产环境中从系统环境变量中获取配置
            this.appId = System.getenv("SPARK_APP_ID");
            this.apiKey = System.getenv("SPARK_API_KEY");
            this.apiSecret = System.getenv("SPARK_API_SECRET");
        }

        this.apiHost = AiModelEnum.SPARK_MAX.getHost();
        this.model = AiModelEnum.SPARK_MAX.getModel();

        // 创建 ResponseBodyEmitter 实例
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        // 异步执行聊天处理逻辑
        CompletableFuture.runAsync(() -> {
            String prompt = chatRequest.getPrompt();

            // 封装 Spark 请求参数
            AiChatRequestHandler aiChatRequestHandler = new AiChatRequestHandler();
            SparkRequest sparkRequest = aiChatRequestHandler.handle(prompt, appId, model);

            // 创建 SparkDeskClient 实例用于与 Spark 模型通信
            SparkAiSocketClient sparkAiSocketClient = SparkAiSocketClient.builder()
                    .host(apiHost)
                    .appid(appId)
                    .apiKey(apiKey)
                    .apiSecret(apiSecret)
                    .build();

            // 发起聊天请求并设置回调
            sparkAiSocketClient.chat(new SparkListener(sparkRequest) {
                @Override
                public void onChatOutput(SparkResponse sparkResponse) {
                    try {
                        String content = sparkResponse.getPayload().getChoices().getText().get(0).getContent();
                        log.info("content: {}", content);
                        // 将响应内容发送给客户端
                        emitter.send(content);
                    } catch (Exception e) {
                        // 发生错误时完成响应并抛出异常
                        emitter.completeWithError(e);
                    }
                }
                @Override
                public void onChatEnd(List<SparkResponse> sparkResponseList) {
                    emitter.complete();
                }
            });
        });

        return emitter;
    }
}
