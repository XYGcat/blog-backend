package com.xc.blogbackend.common;

import lombok.Data;

/**
 * 封装接口返回数据
 * @param <T>
 */
@Data
public class RollApiResponse<T> {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;
}
