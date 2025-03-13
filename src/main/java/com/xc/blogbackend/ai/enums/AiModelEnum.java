package com.xc.blogbackend.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 大模型枚举
 *
 * @author xc
 */
@Getter
@AllArgsConstructor
public enum AiModelEnum {

    // 星火大模型
    SPARK_MAX("generalv3.5", "https://spark-api.xf-yun.com/v3.5/chat"),

    // OPENAI
    OPENAI("OPENAI", "https://api.openai.com/v1/chat/completions"),

    // 阿里云百炼
    ALIYUN_DEEPSEEK_V3("deepseek-v3", "https://dashscope.aliyuncs.com/compatible-mode/v1"),
    ALIYUN_DEEPSEEK_R1("deepseek-r1", "https://dashscope.aliyuncs.com/compatible-mode/v1"),

    ;

    private final String model;
    private final String host;

    /**
     * 根据模型获取host
     *
     * @param model 模型
     * @return host
     */
    public static String getHost(String model) {
        for (AiModelEnum value : values()) {
            if (value.getModel().equals(model)) {
                return value.getHost();
            }
        }
        return null;
    }

    /**
     * 根据模型获取name
     *
     * @param model 模型
     * @return name
     */
    public static String getName(String model) {
        for (AiModelEnum value : values()) {
            if (value.getModel().equals(model)) {
                // 截取第一个下划线之前的部分
                return value.name().split("_")[0];
            }
        }
        return null;
    }
}
