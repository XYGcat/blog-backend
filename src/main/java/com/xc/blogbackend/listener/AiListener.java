package com.xc.blogbackend.listener;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xc.blogbackend.enums.AiStatus;
import com.xc.blogbackend.model.domain.ai.AiReqDto;
import com.xc.blogbackend.model.domain.ai.AiResDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
public abstract class AiListener extends WebSocketListener {

    private List<AiResDto> aiResDtoList = new ArrayList<>();
    private AiReqDto aiReqDto;

    // 提前初始化 ObjectMapper，提高性能
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 构造方法，传入大模型参数
     * @param aiReqDto 大模型请求参数
     */
    public AiListener(AiReqDto aiReqDto) {
        this.aiReqDto = Objects.requireNonNull(aiReqDto, "aiReqDto 不能为空");
    }

    /**
     * WebSocket 连接成功，发送请求
     */
    @Override
    public final void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        log.info("WebSocket 连接成功");
        super.onOpen(webSocket, response); // 调用父类方法
        AiReqDto request = (aiReqDto != null) ? aiReqDto : this.getAiReqDto();

        try {
            String jsonRequest = OBJECT_MAPPER.writeValueAsString(request);
            webSocket.send(jsonRequest);
        } catch (Exception e) {
            log.error("发送 WebSocket 请求失败", e);
            webSocket.close(1001, "JSON 解析异常");
        }
    }

    /**
     * 处理接收到的文本消息
     */
    @Override
    public final void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        AiResDto response = JSONUtil.toBean(text, AiResDto.class);

        if (!isSuccess(response)) {
            handleError(webSocket, response);
            return;
        }

        aiResDtoList.add(response);
        onChatOutput(response);

        if (isEnd(response)) {
            closeWebSocket(webSocket, "星火模型返回结束");
            onChatEnd(aiResDtoList);
            onChatToken(response.getPayload().getUsage());
        }
    }

    /**
     * 处理接收到的字节消息（默认不做处理，可在子类重写）
     */
    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
        log.info("收到二进制数据，长度: {}", bytes.size());
    }

    /**
     * WebSocket 发生错误时的处理
     */
    @Override
    public final void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        log.error("WebSocket 连接失败", t);
        closeWebSocket(webSocket, "WebSocket 失败");
        onWebSocketError(t, response);
    }

    /**
     * 判断请求是否成功
     */
    private boolean isSuccess(AiResDto response) {
        return response != null && response.getHeader() != null &&
                AiResDto.OutHeader.Code.SUCCESS.getValue() == response.getHeader().getCode();
    }

    /**
     * 判断是否结束
     */
    private boolean isEnd(AiResDto response) {
        return response != null && response.getHeader() != null &&
                AiStatus.END.getValue() == response.getHeader().getStatus();
    }

    /**
     * 处理错误信息
     */
    private void handleError(WebSocket webSocket, AiResDto response) {
        log.warn("调用大模型发生错误，错误码: {}，请求 ID: {}",
                response.getHeader().getCode(), response.getHeader().getSid());
        closeWebSocket(webSocket, "大模型调用异常");
        onChatError(response);
    }

    /**
     * 关闭 WebSocket
     */
    private void closeWebSocket(WebSocket webSocket, String reason) {
        if (webSocket != null) {
            webSocket.close(1000, reason);
        }
    }

    // --- 抽象方法，可由子类实现 ---

    /**
     * 处理 AI 模型返回的数据
     */
    public abstract void onChatOutput(AiResDto aiResDto);

    /**
     * 处理对话结束
     */
    public abstract void onChatEnd(List<AiResDto> aiResDtoList);

    /**
     * 记录 token 消耗情况（可选实现）
     */
    public void onChatToken(AiResDto.Usage usage) {
        log.info("Token 消耗情况: {}", usage);
    }

    /**
     * 发送请求时的自定义参数（可选实现）
     */
    public AiReqDto onChatSend() {
        return this.aiReqDto;
    }

    /**
     * WebSocket 连接错误（可选实现）
     */
    public void onWebSocketError(Throwable t, Response response) {
        log.error("WebSocket 发生异常", t);
    }

    /**
     * 处理 AI 模型错误响应（可选实现）
     */
    public void onChatError(AiResDto aiResDto) {
        log.warn("AI 模型返回错误: {}", aiResDto);
    }
}
