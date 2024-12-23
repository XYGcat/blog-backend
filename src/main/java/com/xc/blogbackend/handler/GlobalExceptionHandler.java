package com.xc.blogbackend.handler;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse> handleBusinessException(BusinessException ex) {
        // 使用 ResultUtils 构造统一的错误响应
        BaseResponse response = ResultUtils.error(ex.getCode(), ex.getMessage(), "业务错误");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 返回 400 错误
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleException(Exception ex) {
        BaseResponse<Object> response = new BaseResponse<>(5000, null, "内部服务器错误", "");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500 错误
    }
}