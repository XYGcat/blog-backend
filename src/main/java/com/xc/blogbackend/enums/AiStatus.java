package com.xc.blogbackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI状态枚举类
 */
@Getter
@AllArgsConstructor
public enum AiStatus {

    START(0), // 起始状态，取值为0。

    ING(1), // 进行中状态，取值为1。

    END(2), // 结束状态，取值为2。

    ;

    private final int value; // 状态值。
}
