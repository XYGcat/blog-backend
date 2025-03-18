package com.xc.blogbackend.ai.model.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xc.blogbackend.ai.enums.AiModelEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述： chat模型基础类
 *
 * @author https:www.unfbx.com
 * @since 1.1.2
 * 2023-11-10
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BaseChatCompletion implements Serializable {

    @NonNull
    @Builder.Default
    private String model = AiModelEnum.OPENAI_GPT_3_5_TURBO.getModel();

    /**
     * 指定模型必须输出的格式的对象。
     */
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    /**
     * 使用什么取样温度，0到2之间。较高的值(如0.8)将使输出更加随机，而较低的值(如0.2)将使输出更加集中和确定。
     */
    @Builder.Default
    private double temperature = 0.2;

    /**
     * 使用温度采样的替代方法称为核心采样，其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的代币。
     */
    @JsonProperty("top_p")
    @Builder.Default
    private Double topP = 1d;

    /**
     * 为每个提示生成的完成次数
     */
    @Builder.Default
    private Integer n = 1;

    /**
     * 是否流式输出.
     */
    @Builder.Default
    private boolean stream = false;

    /**
     * 停止输出标识
     */
    private List<String> stop;

    /**
     * 最大支持4096
     */
    @JsonProperty("max_tokens")
    @Builder.Default
    private Integer maxTokens = 2048;


    @JsonProperty("presence_penalty")
    @Builder.Default
    private double presencePenalty = 0;

    /**
     * -2.0 ~~ 2.0
     */
    @JsonProperty("frequency_penalty")
    @Builder.Default
    private double frequencyPenalty = 0;

    @JsonProperty("logit_bias")
    private Map logitBias;

    /**
     * 用户唯一值，确保接口不被重复调用
     */
    private String user;

    private Integer seed;

    private Boolean logprobs;

    @JsonProperty("top_logprobs")
    private Integer topLogprobs;
}
