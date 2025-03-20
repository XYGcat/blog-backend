package com.xc.blogbackend.ai.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xc.blogbackend.ai.enums.AiErrorEnum;
import com.xc.blogbackend.ai.enums.AiModelEnum;
import com.xc.blogbackend.ai.interceptor.DefaultOpenAiAuthInterceptor;
import com.xc.blogbackend.ai.interceptor.OpenAiAuthInterceptor;
import com.xc.blogbackend.ai.model.openai.BaseChatCompletion;
import com.xc.blogbackend.ai.model.openai.ChatCompletion;
import com.xc.blogbackend.ai.model.openai.Message;
import com.xc.blogbackend.exception.BusinessException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * open ai 客户端
 */
@Slf4j
public class OpenAiStreamClient {
    @Getter
    @NotNull
    private String apiKey;

    /**
     * 自定义api host使用builder的方式构造client
     */
    @Getter
    private String apiHost;

    /**
     * 自定义的okHttpClient
     */
    @Getter
    private OkHttpClient okHttpClient;

    /**
     * 自定义鉴权处理拦截器
     * 可以不设置，默认实现：DefaultOpenAiAuthInterceptor
     */
    @Getter
    private OpenAiAuthInterceptor authInterceptor;

    /**
     * 构造实例对象
     *
     * @param builder
     */
    private OpenAiStreamClient(Builder builder) {
        if (StringUtils.isEmpty(builder.apiKey)) {
            throw new BusinessException(AiErrorEnum.API_KEYS_NOT_NULL);
        }
        apiKey = builder.apiKey;

        if (StrUtil.isBlank(builder.apiHost)) {
            builder.apiHost = AiModelEnum.OPENAI_GPT_3_5_TURBO.getHost();
        }
        apiHost = builder.apiHost;

        if (Objects.isNull(builder.authInterceptor)) {
            builder.authInterceptor = new DefaultOpenAiAuthInterceptor();
        }
        authInterceptor = builder.authInterceptor;
        //设置apiKeys和key的获取策略
        authInterceptor.setApiKey(this.apiKey);

        if (Objects.isNull(builder.okHttpClient)) {
            builder.okHttpClient = this.okHttpClient();
        } else {
            //自定义的okhttpClient
            builder.okHttpClient = builder.okHttpClient
                    .newBuilder()
                    .build();
        }
        okHttpClient = builder.okHttpClient;
    }

    /**
     * 构造
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private @NotNull String apiKey;

        /**
         * api请求地址，结尾处有斜杠
         */
        private String apiHost;

        /**
         * 自定义OkhttpClient
         */
        private OkHttpClient okHttpClient;

        /**
         * 自定义鉴权拦截器
         */
        private OpenAiAuthInterceptor authInterceptor;

        public Builder() {
        }

        public Builder apiKey(@NotNull String val) {
            apiKey = val;
            return this;
        }

        public Builder apiHost(String val) {
            apiHost = val;
            return this;
        }

        public Builder okHttpClient(OkHttpClient val) {
            okHttpClient = val;
            return this;
        }

        public Builder authInterceptor(OpenAiAuthInterceptor val) {
            authInterceptor = val;
            return this;
        }

        public OpenAiStreamClient build() {
            return new OpenAiStreamClient(this);
        }
    }

    /**
     * 创建默认的OkHttpClient
     */
    private OkHttpClient okHttpClient() {
        if (Objects.isNull(this.authInterceptor)) {
            this.authInterceptor = new DefaultOpenAiAuthInterceptor();
        }
        this.authInterceptor.setApiKey(this.apiKey);
        return new OkHttpClient
                .Builder()
                .addInterceptor(this.authInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 流式输出
     *
     * @param chatCompletion      问答参数
     * @param eventSourceListener sse监听器
     */
    public <T extends BaseChatCompletion> void streamChatCompletion(T chatCompletion, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空");
            throw new BusinessException(AiErrorEnum.PARAM_ERROR);
        }
        if (!chatCompletion.isStream()) {
            chatCompletion.setStream(true);
        }
        try {
            EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(chatCompletion);
            Request request = new Request.Builder()
                    .url(this.apiHost)
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                    .build();
            //创建事件
            EventSource eventSource = factory.newEventSource(request, eventSourceListener);
        } catch (JsonProcessingException e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        }
    }

    /**
     * 流式输出
     * 警告：（不支持图片输入）
     *
     * @param messages            问答列表
     * @param eventSourceListener sse监听器
     */
    public void streamChatCompletion(List<Message> messages, EventSourceListener eventSourceListener) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(messages)
                .stream(true)
                .build();
        this.streamChatCompletion(chatCompletion, eventSourceListener);
    }
}
