package com.xc.blogbackend.ai.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * OpenAI请求体
 */
@Data
@AllArgsConstructor
public class OpenAiRequest {

    @JsonProperty("model")
    private String model;

    @JsonProperty("messages")
    private List<Message> messages;

    @JsonProperty("stream")
    private Boolean stream;

    @JsonProperty("stream_options")
    private StreamOptions streamOptions;

    @Data
    @AllArgsConstructor
    public static class Message {
        @JsonProperty("role")
        private String role;

        @JsonProperty("content")
        private String content;
    }

    @Data
    @AllArgsConstructor
    public static class StreamOptions {

        @JsonProperty("include_usage")
        private Boolean includeUsage;
    }
}