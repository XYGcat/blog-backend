package com.xc.blogbackend.common;

/**
 * 返回工具类
 *
 * @author 星尘
 */
public class ResultUtils {
    /**
     * 成功
     * @param result
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T result){
        return new BaseResponse<>(0,result,"OK");
    }

    public static <T> BaseResponse<T> success(T result, String message){
        return new BaseResponse<>(0,result,message);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code,String message,String description){
        return new BaseResponse<>(code,null,message,description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse<>(errorCode.getCode(),null,message,description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse<>(errorCode.getCode(),errorCode.getMessage(),description);
    }
}
