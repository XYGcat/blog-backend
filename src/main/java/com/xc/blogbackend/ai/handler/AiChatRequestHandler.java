package com.xc.blogbackend.ai.handler;

import com.xc.blogbackend.ai.model.spark.Chat;
import com.xc.blogbackend.ai.model.spark.SparkRequest;
import com.xc.blogbackend.ai.model.spark.Text;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * AiChat请求参数封装处理
 */
public class AiChatRequestHandler {

    /**
     * 处理用户输入，生成 SparkRequest
     *
     * @param msg 用户输入
     */
    public SparkRequest handle(String msg, String appId, String model){
        // 构建请求头信息
        SparkRequest.InHeader header = SparkRequest.InHeader.builder()
                .uid(UUID.randomUUID().toString().substring(0, 10)) // 设置用户ID为随机生成的10位字符串
                .appid(appId) // 设置应用ID
                .build();

        // 构建请求参数信息
        SparkRequest.Parameter parameter = SparkRequest.Parameter.builder()
                .chat(Chat.builder()
                        .domain(model) // 设置访问的领域为V3.5版本
                        .maxTokens(2048) // 设置最大token数为2048
                        .temperature(0.3) // 设置温度为0.3
                        .build())
                .build(); // 构建Parameter实例

        // 构建请求消息内容
        List<Text> text = Arrays.asList(
                Text.builder().role(Text.Role.SYSTEM.getName()).content(
                        "").build(),
                Text.builder().role(Text.Role.USER.getName()).content(msg).build()
        );
        SparkRequest.InPayload payload = SparkRequest.InPayload.builder()
                .message(SparkRequest.InPayload.Message.builder()
                        .text(text) // 设置文本内容列表
                        .build())
                .build();

        // 构建AiReqDto请求对象
        SparkRequest sparkRequest = SparkRequest.builder()
                .header(header) // 设置请求头
                .parameter(parameter) // 设置请求参数
                .payload(payload) // 设置请求消息内容
                .build();

        return sparkRequest;
    }
}
