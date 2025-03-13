package com.xc.blogbackend.ai.model.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * OpenAI 响应体
 */
@Data
public class OpenAiResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created")
    private Long created;

    @JsonProperty("model")
    private String model;

    @JsonProperty("choices")
    private List<Choice> choices;

    @JsonProperty("usage")
    private Usage usage;

    @Data
    public static class Delta {

        @JsonProperty("content")
        private String content;

        @JsonProperty("reasoning_content")
        private String reasoningContent;

        @JsonProperty("role")
        private String role;
    }

    @Data
    public static class Choice {

        @JsonProperty("delta")
        private Delta delta;

        @JsonProperty("index")
        private Integer index;

        @JsonProperty("finish_reason")
        private String finishReason;

        @JsonProperty("logprobs")
        private Object logprobs;
    }

    @Data
    public static class Usage {

        @JsonProperty("prompt_tokens")
        private Integer promptTokens;

        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }
}
