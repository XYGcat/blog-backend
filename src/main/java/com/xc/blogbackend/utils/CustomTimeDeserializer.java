package com.xc.blogbackend.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的反序列化器来处理空字符串
 *
 * @author 星尘
 */

// 自定义反序列化器，继承自 StdDeserializer 类
public class CustomTimeDeserializer extends StdDeserializer<List<String>> {
    public CustomTimeDeserializer() {
        this(null);
    }

    public CustomTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    // 重写反序列化方法
    @Override
    public List<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        // 从 JsonParser 中读取 JSON 数据
        JsonNode node = jp.getCodec().readTree(jp);

        // 如果 JSON 数据为 null 或者为空字符串，则返回一个空的 ArrayList
        if (node.isNull() || node.asText().isEmpty()) {
            return new ArrayList<>();
        }

        // 如果需要进行特定的解析逻辑，请在此处添加代码
        // 例如，如果字段应包含字符串数组，则可以相应地解析

        // 最终返回一个空的 ArrayList，如果字段不为 null 但为空，则返回空列表
        return new ArrayList<>();
    }
}
