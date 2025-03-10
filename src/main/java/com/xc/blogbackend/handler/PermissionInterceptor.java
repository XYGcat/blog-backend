package com.xc.blogbackend.handler;

import com.xc.blogbackend.exception.StopMatchException;
import com.xc.blogbackend.strategy.XcAnnotationStrategy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 *
 * @author xc
 */
public class PermissionInterceptor implements HandlerInterceptor {

    /**
     * 每次请求之前触发的方法
     */
    @Override
    @SuppressWarnings("all")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        try {
            // 这里必须确保 handler 是 HandlerMethod 类型时，才能进行注解鉴权
            if(handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                XcAnnotationStrategy.instance.checkMethodAnnotation(handlerMethod);
            }

        } catch (StopMatchException e) {
            // StopMatchException 异常代表：停止匹配，进入Controller
        }

        // 通过验证
        return true;
    }
}
