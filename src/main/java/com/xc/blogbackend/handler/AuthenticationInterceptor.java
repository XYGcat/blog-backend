package com.xc.blogbackend.handler;

import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.BlogUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.xc.blogbackend.contant.BlogUserConstant.statusExcludedPaths;

/**
 * 身份验证拦截器
 *
 * @author 星尘
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    // 进入controller层之前拦截请求
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // 排除特定的请求路径
        if (statusExcludedPaths.stream().anyMatch(requestURI::contains)) {
            return true; // 放行特定路径的请求
        }

        //获取登录验证拦截器传递的用户信息
        BlogUser user = (BlogUser) request.getAttribute("user");

        if (user.getRole() != 1) {
            throw new BusinessException(401,"普通用户仅限查看");
        }

        return true;
    }

    // 在请求处理之后进行拦截
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 可以在这里添加一些针对请求的后处理逻辑
    }

    // 在整个请求完成后进行拦截
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 可以在这里进行一些资源清理操作等
    }
}
