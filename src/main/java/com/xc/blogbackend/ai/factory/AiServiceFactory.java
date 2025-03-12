package com.xc.blogbackend.ai.factory;

import com.xc.blogbackend.ai.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * AI 服务工厂类，用于根据模型标识获取对应的 AI 服务实现
 */
@Component
public class AiServiceFactory {

    private final Map<String, AiService> aiServices;

    @Autowired
    public AiServiceFactory(ApplicationContext context) {
        // 获取所有 AiService 的 Bean，key 为 bean 名称
        this.aiServices = context.getBeansOfType(AiService.class);
    }

    /**
     * 根据模型标识获取对应的 AI 服务实现
     *
     * @param model 模型标识，例如 "OPENAI" 或 "SPARKAI"
     * @return 对应的 AiService 实例
     * @throws IllegalArgumentException 如果未找到相应的服务
     */
    public AiService getAiService(String model) {
        AiService service = aiServices.get(model.toUpperCase());
        if (service == null) {
            throw new IllegalArgumentException("No AI service found for model: " + model);
        }
        return service;
    }
}
