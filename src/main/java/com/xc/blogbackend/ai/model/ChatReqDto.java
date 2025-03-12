package com.xc.blogbackend.ai.model;

import lombok.Data;

/**
 * 聊天请求参数
 */
@Data
public class ChatReqDto {

    // 聊天消息
    private String prompt;

    // 上下文
    private ChatContext options;

    // 系统消息
    private String systemMessage;

    // 温度
    private int temperature;

    // 置信度
    private int top_p;

    @Data
    public static class ChatContext {
        // 会话ID
        private String conversationId;

        // 消息ID
        private String parentMessageId;
    }
}
