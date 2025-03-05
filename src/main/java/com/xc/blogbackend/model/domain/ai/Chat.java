package com.xc.blogbackend.model.domain.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 聊天参数
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // 指示在反序列化时忽略未知属性。
public class Chat {
    /**
     * 取值为[general,generalv2,generalv3,generalv3.5]
     * 指定访问的领域,generalv3.5指向V3.5版本。注意：不同的取值对应的url也不一样
     */
    private String domain;

    /**
     * 温度，用于控制模型生成结果的多样性，取值范围为0到1，默认为0.5
     */
    private double temperature;

    /**
     * 最大令牌数，用于控制模型生成的最大长度，取值范围为1到4096，默认为2048。
     */
    @JsonProperty("max_tokens") // 指定JSON属性名称为"max_tokens"
    private Integer maxTokens;

    /**
     * 取值为[1，6],默认为4
     * 从k个候选中随机选择⼀个（⾮等概率）
     */
    @JsonProperty("top_k") // 指定JSON属性名称为"top_k"
    private Integer topK;

    /**
     *  聊天ID，用于保障用户下的唯一性。
     */
    @JsonProperty("chat_id") // 指定JSON属性名称为"chat_id"
    private String chatId;
}
