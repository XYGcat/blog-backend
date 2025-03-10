package com.xc.blogbackend.exception;

/**
 * 停止匹配,进入 Controller
 *
 * @author xc
 */
public class StopMatchException extends RuntimeException {

    public StopMatchException(String message) {
        super(message);
    }
}
