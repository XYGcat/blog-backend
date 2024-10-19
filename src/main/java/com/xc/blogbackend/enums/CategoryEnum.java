package com.xc.blogbackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryEnum {
    ARTICLE(1,"文章分类"),
    BLOG(2,"资源导航分类"),
    ;

    private Integer code;
    private String description;
}
