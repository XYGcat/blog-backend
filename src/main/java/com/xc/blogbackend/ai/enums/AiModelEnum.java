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
    SPARK_MAX("SPARK_MAX", "https://spark-api.xf-yun.com/v3.5/chat"),

    // 阿里云百炼
    ALIYUN_DEEPSEEK_V3("deepseek-v3", "https://dashscope.aliyuncs.com/compatible-mode/v1"),
    ALIYUN_DEEPSEEK_R1("deepseek-r1", "https://dashscope.aliyuncs.com/compatible-mode/v1"),

    ;

    private final String model;
    private final String host;
}
