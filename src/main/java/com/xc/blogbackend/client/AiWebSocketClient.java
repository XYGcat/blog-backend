package com.xc.blogbackend.client;

import com.xc.blogbackend.listener.AiListener;
import com.xc.blogbackend.utils.AuthUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * WebSocket 客户端封装类
 */
@Data
@Slf4j
public class AiWebSocketClient {
    private String host; // WebSocket 服务器地址
    private String appid; // 应用 ID
    private String apiKey; // API 密钥
    private String apiSecret; // API 密钥密钥
    private OkHttpClient okHttpClient; // OkHttp WebSocket 客户端

    /**
     * 静态工厂方法，返回 Builder 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder 设计模式，用于构造 AiWebSocketClient 实例
     */
    public static final class Builder {
        private String host;
        private String appid;
        private String apiKey;
        private String apiSecret;
        private OkHttpClient okHttpClient;

        private Builder() {}

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder appid(String appid) {
            this.appid = appid;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder apiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }

        public Builder okHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        /**
         * 构建 AiWebSocketClient 实例
         */
        public AiWebSocketClient build() {
            AiWebSocketClient aiWebSocketClient = new AiWebSocketClient();
            aiWebSocketClient.host = this.host;
            aiWebSocketClient.appid = this.appid;
            aiWebSocketClient.apiKey = this.apiKey;
            aiWebSocketClient.apiSecret = this.apiSecret;

            // 确保 OkHttpClient 不为空
            if (this.okHttpClient == null) {
                this.okHttpClient = new OkHttpClient.Builder().build();
            }
            aiWebSocketClient.okHttpClient = this.okHttpClient;
            return aiWebSocketClient;
        }
    }

    /**
     * 创建 WebSocket 连接，绑定 AiListener
     * @param aiListener 监听器
     * @return WebSocket 连接实例
     */
    @SneakyThrows
    public <T extends AiListener> WebSocket chat(T aiListener) {
        try {
            // 获取鉴权后的 WebSocket 连接地址
            String authUrl = AuthUtils.getAuthUrl(host, apiKey, apiSecret);
            String url = authUrl.startsWith("https")
                    ? authUrl.replaceFirst("https", "wss")
                    : authUrl.replaceFirst("http", "ws");

            // 构造 WebSocket 请求
            Request request = new Request.Builder().url(url).build();

            // 创建 WebSocket 连接并绑定监听器
            return this.okHttpClient.newWebSocket(request, aiListener);
        } catch (Exception e) {
            log.error("WebSocket 连接失败: {}", e.getMessage(), e);
            throw e;
        }
    }
}
