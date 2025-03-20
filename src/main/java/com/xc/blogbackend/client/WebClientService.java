package com.xc.blogbackend.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *  WebClient 请求工具类
 */
@Service
public class WebClientService {

    private final WebClient webClient;

    public WebClientService() {
        // 创建 WebClient 实例
        this.webClient = WebClient.create();
    }

    /**
     * 发送 GET 请求
     * @param url 请求的 URL
     * @param responseType 响应的类型
     * @param <T> 泛型返回类型
     * @return 响应体的封装对象
     */
    public <T> T get(String url, ParameterizedTypeReference<T> responseType) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType)
                .block(); // 阻塞等待返回
    }
}
