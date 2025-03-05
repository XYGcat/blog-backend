package com.xc.blogbackend.model.domain.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * AI响应数据模型
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiResDto {

    // OutHeader对象，表示响应的头部信息
    private OutHeader header;

    // OutPayload对象，表示响应的载荷信息
    private OutPayload payload;

    /**
     * OutHeader对象，表示响应的头部信息
     */
    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OutHeader {
        /**
         * 错误码，0表示正常，非0表示出错；详细释义可在接口说明文档最后的错误码说明了解
         * https://www.xfyun.cn/doc/spark/%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E.html
         */
        private int code;

        /**
         * 会话状态，取值为[0,1,2]；0代表首次结果；1代表中间结果；2代表最后一个结果
         */
        private int status;

        /**
         * 会话是否成功的描述信息
         */
        private String message;

        /**
         * 会话的唯一id，用于讯飞技术人员查询服务端会话日志使用,出现调用错误时建议留存该字段
         */
        private String sid;

        /**
         * 错误码，0表示正常，非0表示出错
         */
        @Getter
        @AllArgsConstructor
        public enum Code { // Code枚举声明

            SUCCESS(0), // 成功状态，错误码为0。

            ;

            private final int value; // 错误码的值。
        }
    }


    /**
     * OutPayload对象，表示响应的载荷信息
     */
    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class OutPayload {

        /**
         * 问答信息,用于表示选择的响应
         */
        private Choices choices;

        /**
         * Token 信息,用于表示使用情况
         */
        private Usage usage;
    }

    /**
     * Choices对象，表示选择的响应
     */
    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Choices {

        /**
         * 文本响应状态，取值为[0,1,2]; 0代表首个文本结果；1代表中间文本结果；2代表最后一个文本结果
         */
        private Integer status;

        /**
         * 返回的数据序号，取值为[0,9999999]
         */
        private Integer seq;

        /**
         * 返回信息
         */
        private List<Text> text;
    }

    /**
     * Usage对象，表示使用情况
     */
    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Usage {

        // 文本，用于表示使用情况中的文本信息
        private Text text;

        @Data
        @AllArgsConstructor
        public class Text {

            @JsonProperty("question_tokens")
            private Integer questionTokens; // 问题标记数，用于表示问题中的标记数量。

            @JsonProperty("prompt_tokens")
            private Integer promptTokens; // 提示标记数，用于表示提示中的标记数量。

            @JsonProperty("completion_tokens")
            private Integer completionTokens; // 完成标记数，用于表示完成中的标记数量。

            @JsonProperty("total_tokens")
            private Integer totalTokens; // 总标记数，用于表示总共的标记数量。
        }
    }

}
