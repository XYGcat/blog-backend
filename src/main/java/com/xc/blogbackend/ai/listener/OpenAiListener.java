package com.xc.blogbackend.ai.listener;

import com.xc.blogbackend.ai.model.openai.OpenAiRequest;
import com.xc.blogbackend.ai.model.openai.OpenAiResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * OpenAI 监听器
 *
 * @author xc
 */
@Slf4j
@Getter
public abstract class OpenAiListener extends AiListener<OpenAiRequest, OpenAiResponse> {

    /**
     * 构造方法，传入大模型参数
     * @param openAIRequest 大模型请求参数
     */
    public OpenAiListener(OpenAiRequest openAIRequest) {
        super(openAIRequest);
    }

    /**
     * 定义返回体的类型信息
     */
    @Override
    protected Class<OpenAiResponse> getResponseType() {
        return OpenAiResponse.class;
    }

    /**
     * 处理 AI 返回的消息
     */
    @Override
    public void onChatOutput(OpenAiResponse response) {
        log.info("模型输出: {}", response);
    }

    /**
     * 处理对话结束
     */
    @Override
    public void onChatEnd(List<OpenAiResponse> responseList) {
        log.info("对话结束，总消息数量: {}", responseList.size());
    }

    /**
     * 判断请求是否成功
     */
    @Override
    protected boolean isSuccess(OpenAiResponse response) {
        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
            OpenAiResponse.Choice choice = response.getChoices().get(0);

            if (choice.getFinishReason() == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否对话结束
     */
    @Override
    protected boolean isEnd(OpenAiResponse response) {
        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
            OpenAiResponse.Choice choice = response.getChoices().get(0);

            if ("stop".equals(choice.getFinishReason())) {
                System.out.println("对话结束");
            } else if ("length".equals(choice.getFinishReason())) {
                System.out.println("生成长度达到最大限制");
            } else if ("content_filter".equals(choice.getFinishReason())) {
                System.out.println("内容被屏蔽");
            }
            return true;
        } else {
            return false;
        }
    }
}
