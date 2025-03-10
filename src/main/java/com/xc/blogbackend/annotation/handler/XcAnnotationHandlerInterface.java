package com.xc.blogbackend.annotation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 所有权限注解处理器的父接口
 *
 * @author click33
 * @since 2024/8/2
 */
public interface XcAnnotationHandlerInterface<T extends Annotation> {

    /**
     * 获取所要处理的注解类型
     * @return /
     */
    Class<T> getHandlerAnnotationClass();

    /**
     * 所需要执行的校验方法
     * @param at 注解对象
     * @param method 被标注的注解的方法引用
     */
    @SuppressWarnings("unchecked")
    default void check(Annotation at, Method method) {
        checkMethod((T) at, method);
    }

    /**
     * 所需要执行的校验方法（转换类型后）
     * @param at 注解对象
     * @param method 被标注的注解的方法引用
     */
    void checkMethod(T at, Method method);
}
