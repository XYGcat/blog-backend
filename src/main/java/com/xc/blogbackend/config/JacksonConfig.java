package com.xc.blogbackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Jackson 配置类，用于自定义序列化和反序列化
 */
@Configuration // 声明这是一个配置类
public class JacksonConfig {

    @Bean // 声明这个方法返回一个 Bean，Spring 会管理这个 Bean 的生命周期
    @Primary // 表示这个 Bean 是首选的，如果容器中有多个同类型的 Bean，将优先使用这个
    @ConditionalOnMissingBean(ObjectMapper.class) // 仅在容器中没有 ObjectMapper 类型的 Bean 时创建这个 Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        // 使用 Jackson2ObjectMapperBuilder 创建 ObjectMapper 实例
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 创建一个 SimpleModule，用于自定义序列化和反序列化
        SimpleModule simpleModule = new SimpleModule();

        // 自定义 Long 类型的序列化方式：将 Long 类型转换为 String 类型
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);

        // 将自定义的模块注册到 ObjectMapper 中
        objectMapper.registerModule(simpleModule);

        // 返回配置好的 ObjectMapper 实例
        return objectMapper;
    }
}
