package com.xc.blogbackend.model.domain.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * 文本内容
 */
@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Text {
    /**
     * 取值为[system,user,assistant]，system用于设置对话背景，user表示是用户的问题，assistant表示AI的回复
     */
    private String role;

    /**
     * 内容，用于表示文本的内容.所有content的累计tokens需控制8192以内
     */
    private String content;

    @Getter
    public enum Role { // Role枚举声明

        USER("user"), // 用户角色

        ASSISTANT("assistant"), // 助手角色

        SYSTEM("system") //系统角色

        ;

        private final String name; // 角色名称。

        Role(String name) { // 构造函数，初始化角色名称。
            this.name = name;
        }
    }
}

