package com.xc.blogbackend.ai.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xc.blogbackend.ai.model.openai.OpenAiResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Objects;

@Slf4j
public class OpenAISSEEventSourceListener<T extends ResponseBodyEmitter> extends EventSourceListener {

    private final T emitter;

    public OpenAISSEEventSourceListener(T emitter) {
        this.emitter = emitter;
    }

    /**
     * 建立sse连接
     * @param eventSource
     * @param response
     */
    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("OpenAI建立sse连接...");
    }

    /**
     * 接收到sse数据
     * @param eventSource
     * @param id
     * @param type
     * @param data
     */
    @SneakyThrows
    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        log.info("OpenAI返回数据：{}", data);
        if (data.equals("[DONE]")) {
            log.info("OpenAI返回数据结束了");
            // 传输完成后自动关闭sse
            emitter.complete();
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        OpenAiResponse openAiResponse = mapper.readValue(data, OpenAiResponse.class); // 读取Json
        try {
            emitter.send(openAiResponse.getChoices().get(0).getDelta().getContent());
        } catch (Exception e) {
            log.error("sse信息推送失败！");
            eventSource.cancel();
            e.printStackTrace();
        }
    }

    /**
     * 关闭sse连接
     * @param eventSource
     */
    @Override
    public void onClosed(EventSource eventSource) {
        log.info("OpenAI关闭sse连接...");
        emitter.complete();
    }

    /**
     * sse连接异常
     * @param eventSource
     * @param t
     * @param response
     */
    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if (Objects.isNull(response)) {
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
        emitter.completeWithError(t);
    }
}
