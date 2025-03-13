package com.xc.blogbackend.ai.listener;

import com.xc.blogbackend.ai.enums.AiStatusEnum;
import com.xc.blogbackend.ai.model.spark.SparkRequest;
import com.xc.blogbackend.ai.model.spark.SparkResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 星火模型监听器
 *
 * @author xc
 */
@Slf4j
@Getter
public abstract class SparkListener extends AiListener<SparkRequest, SparkResponse> {

    /**
     * 构造方法，传入大模型参数
     * @param sparkRequest 大模型请求参数
     */
    public SparkListener(SparkRequest sparkRequest) {
        super(sparkRequest);
    }

    /**
     * 定义返回体的类型信息
     */
    @Override
    protected Class<SparkResponse> getResponseType() {
        return SparkResponse.class;
    }

    /**
     * 处理 AI 返回的消息
     */
    @Override
    public void onChatOutput(SparkResponse response) {
        log.info("模型输出: {}", response);
    }

    /**
     * 处理对话结束
     */
    @Override
    public void onChatEnd(List<SparkResponse> responseList) {
        log.info("对话结束，总消息数量: {}", responseList.size());
    }

    /**
     * 判断请求是否成功
     */
    @Override
    protected boolean isSuccess(SparkResponse response) {
        return response != null &&
                response.getHeader() != null &&
                SparkResponse.OutHeader.Code.SUCCESS.getValue() == response.getHeader().getCode();
    }

    /**
     * 判断是否对话结束
     */
    @Override
    protected boolean isEnd(SparkResponse response) {
        return response != null &&
                response.getHeader() != null &&
                AiStatusEnum.END.getValue() == response.getHeader().getStatus();
    }
}
