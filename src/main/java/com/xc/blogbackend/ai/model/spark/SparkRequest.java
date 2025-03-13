package com.xc.blogbackend.ai.model.spark;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AI请求参数
 */
@Data
@ApiModel(value = "AI请求参数")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // 指示在反序列化时忽略未知属性
public class SparkRequest {

    private InHeader header;
    private Parameter parameter;
    private InPayload payload;

    /**
     * 表示请求头参数
     */
    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InHeader {
        @JsonProperty("app_id") // 指定JSON属性名称为"app_id"
        private String appid; // 应用ID，用于表示应用的唯一标识。

        private String uid; // 用户ID，用于表示用户的唯一标识。
    }

    /**
     * 表示参数
     */
    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Parameter {

        private Chat chat; // 参数，用于表示聊天参数。
    }

    /**
     * 表示输入的载荷信息
     */
    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InPayload {

        private Message message; // 消息，用于表示输入的载荷信息。

        @Data
        @Builder
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Message {
            private List<Text> text; // 文本列表，用于表示消息中的文本信息
        }
    }
}
