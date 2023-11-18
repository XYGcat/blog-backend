package com.xc.blogbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 * @author 星尘
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;

    private T result;

    private String message;

    private String description;

    public BaseResponse(int code, T result, String message, String description) {
        this.code = code;
        this.result = result;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T result, String message) {
        this(code, result, message,"");
    }

    public BaseResponse(int code, T result) {
        this(code, result, "","");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}
