package com.xc.blogbackend.ai.listener;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * 抽象的 AI 监听器，用于处理 AI 模型返回的数据和错误信息。
 * 子类需要实现 isSuccess()、isEnd()、onChatOutput()、onChatEnd() 等抽象方法。
 *
 * @param <REQ>  请求参数类型
 * @param <RES>  响应参数类型
 * @author xc
 */
@Slf4j
@Getter
public abstract class AiListener<REQ, RES> extends WebSocketListener {

    private List<RES> responseList = new ArrayList<>();
    private REQ request;

    // 提前初始化 ObjectMapper，提高性能
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 构造方法，传入泛型请求参数
     */
    public AiListener(REQ request) {
        this.request = Objects.requireNonNull(request, "request 不能为空");
    }

    /**
     * WebSocket 连接成功，发送请求
     */
    @Override
    public final void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        log.info("WebSocket 连接成功");
        super.onOpen(webSocket, response);

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
        try {
            RES response = JSONUtil.toBean(text, getResponseType());
            if (!isSuccess(response)) {
                handleError(webSocket, response);
                return;
            }

            responseList.add(response);
            onChatOutput(response);

            if (isEnd(response)) {
                closeWebSocket(webSocket, "模型返回结束");
                onChatEnd(responseList);
            }
        } catch (Exception e) {
            log.error("解析 WebSocket 消息失败", e);
            closeWebSocket(webSocket, "消息解析失败");
        }
    }

    /**
     * 处理接收到的二进制消息（默认不做处理，可在子类重写）
     */
    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
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
     * 判断请求是否成功（由子类实现）
     */
    protected abstract boolean isSuccess(RES response);

    /**
     * 判断是否结束（由子类实现）
     */
    protected abstract boolean isEnd(RES response);

    /**
     * 处理错误信息
     */
    private void handleError(WebSocket webSocket, RES response) {
        log.warn("调用大模型发生错误: {}", response);
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

    /**
     * 获取响应类型
     * 子类可以实现此方法，返回具体的响应类型
     */
    protected abstract Class<RES> getResponseType();

    // --- 抽象方法，可由子类实现 ---

    /**
     * 处理 AI 模型返回的数据
     */
    public abstract void onChatOutput(RES response);

    /**
     * 处理对话结束
     */
    public abstract void onChatEnd(List<RES> responseList);

    /**
     * 记录 token 消耗情况（可选实现）
     */
    public void onChatToken(Object usage) {
        log.info("Token 消耗情况: {}", usage);
    }

    /**
     * 发送请求时的自定义参数（可选实现）
     */
    public REQ onChatSend() {
        return this.request;
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
    public void onChatError(RES response) {
        log.warn("AI 模型返回错误: {}", response);
    }
}