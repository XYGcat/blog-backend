package com.xc.blogbackend.common;

import com.xc.blogbackend.enums.ErrorCode;

/**
 * 返回工具类
 *
 * @author 星尘
 */
public class ResultUtils {

    /**
     * 成功返回
     * @param data 数据
     * @param <T> 数据类型
     * @return 返回成功的响应
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0, data, "OK");
    }

    /**
     * 成功返回，带自定义消息
     * @param data 数据
     * @param message 自定义消息
     * @param <T> 数据类型
     * @return 返回成功的响应
     */
    public static <T> BaseResponse<T> success(T data, String message){
        return new BaseResponse<>(0, data, message);
    }

    /**
     * 错误返回，使用 ErrorCode
     * @param errorCode 错误码
     * @return 错误响应
     */
    public static BaseResponse error(ErrorCode errorCode){
        return error(errorCode.getCode(), errorCode.getMessage(), errorCode.getDescription());
    }

    /**
     * 错误返回，使用 ErrorCode 并附带自定义 message 和 description
     * @param errorCode 错误码
     * @param message 错误消息
     * @return 错误响应
     */
    public static BaseResponse error(ErrorCode errorCode, String message){
        return error(errorCode.getCode(), message, "");
    }

    /**
     * 错误返回，使用错误码、消息和描述
     * @param code 错误码
     * @param message 错误消息
     * @param description 错误描述
     * @return 错误响应
     */
    public static BaseResponse error(int code, String message, String description){
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     * 错误返回，使用错误码和消息
     * @param code 错误码
     * @param message 错误消息
     * @return 错误响应
     */
    public static BaseResponse error(int code, String message){
        return error(code, message, "");
    }

    /**
     * 错误返回，带自定义消息
     * @param message 错误消息
     * @param description 错误描述
     * @return 错误响应
     */
    public static BaseResponse error(String message, String description){
        return new BaseResponse<>(500, null, message, description);  // 默认为500内部错误
    }
}

