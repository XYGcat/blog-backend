package com.xc.blogbackend.strategy;

import com.xc.blogbackend.annotation.XcCheckRole;
import com.xc.blogbackend.annotation.XcIgnore;
import com.xc.blogbackend.annotation.handler.XcAnnotationHandlerInterface;
import com.xc.blogbackend.annotation.handler.XcCheckRoleHandler;
import com.xc.blogbackend.annotation.handler.XcIgnoreHandler;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 注解鉴权相关策略
 */
public final class XcAnnotationStrategy {

    private XcAnnotationStrategy() {
        registerDefaultAnnotationHandler();
    }

    /**
     * 全局单例引用
     */
    public static final XcAnnotationStrategy instance = new XcAnnotationStrategy();

    /**
     * 注解处理器集合
     */
    public Map<Class<?>, XcAnnotationHandlerInterface<?>> annotationHandlerMap = new LinkedHashMap<>();

    /**
     * 注册所有默认的注解处理器
     */
    public void registerDefaultAnnotationHandler() {
        annotationHandlerMap.put(XcIgnore.class, new XcIgnoreHandler());
        annotationHandlerMap.put(XcCheckRole.class, new XcCheckRoleHandler());
    }

    /**
     * 对一个 [Method] 对象进行注解校验 （注解鉴权内部实现）
     */
    public void checkMethodAnnotation(HandlerMethod handlerMethod) {
        // 遍历所有注册的处理器，分别检查类级和方法级的注解
        for (Map.Entry<Class<?>, XcAnnotationHandlerInterface<?>> entry: annotationHandlerMap.entrySet()) {
            Class<? extends Annotation> annClass = entry.getValue().getHandlerAnnotationClass();

            // 先检查类上的注解
            Annotation ann = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), annClass);
            if (ann != null) {
                entry.getValue().check(ann, handlerMethod.getMethod());
            }
            // 再检查方法上的注解
            ann = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), annClass);
            if (ann != null) {
                entry.getValue().check(ann, handlerMethod.getMethod());
            }
        }
    }
}
