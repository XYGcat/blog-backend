package com.xc.blogbackend.ai;

import com.xc.blogbackend.ai.client.OpenAiStreamClient;
import com.xc.blogbackend.ai.enums.AiModelEnum;
import com.xc.blogbackend.ai.listener.OpenAISSEEventSourceListener;
import com.xc.blogbackend.ai.model.openai.ChatCompletion;
import com.xc.blogbackend.ai.model.openai.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class OpenAiStreamClientTest {

    private OpenAiStreamClient client;

    @BeforeEach
    public void before() {
        client = OpenAiStreamClient.builder()
                .apiKey("")
                .apiHost(AiModelEnum.ALIYUN_DEEPSEEK_V3.getHost())
                .build();
    }

    @Test
    public void chatCompletions() {
        OpenAISSEEventSourceListener eventSourceListener = new OpenAISSEEventSourceListener(null);
        Message message = Message.builder().role("user").content("random one word！").build();
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .model("deepseek-v3")
                .temperature(0.2)
                .maxTokens(2048)
                .messages(Collections.singletonList(message))
                .stream(true)
                .build();
        client.streamChatCompletion(chatCompletion, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
