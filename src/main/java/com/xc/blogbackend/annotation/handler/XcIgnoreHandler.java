package com.xc.blogbackend.annotation.handler;

import com.xc.blogbackend.annotation.XcIgnore;
import com.xc.blogbackend.exception.StopMatchException;

import java.lang.reflect.Method;

/**
 * 注解 SaIgnore 的处理器
 */
public class XcIgnoreHandler implements XcAnnotationHandlerInterface<XcIgnore>{

    @Override
    public Class<XcIgnore> getHandlerAnnotationClass() {
        return XcIgnore.class;
    }

    @Override
    public void checkMethod(XcIgnore at, Method method) {
        _checkMethod();
    }

    public static void _checkMethod() {
        throw new StopMatchException("stop match");
    }
}
