package com.xc.blogbackend.exception;

import com.xc.blogbackend.common.ErrorCode;
import lombok.Getter;

/**
 * 自定义异常类
 *
 * @author 星尘
 */
@Getter
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = -4390225209525539319L;
    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
