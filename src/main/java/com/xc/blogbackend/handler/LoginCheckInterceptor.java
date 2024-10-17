package com.xc.blogbackend.handler;

import cn.hutool.core.util.ObjectUtil;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.BlogUser;
import com.xc.blogbackend.utils.JwtGenerator;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证拦截器
 *
 * @author 星尘
 */
public class LoginCheckInterceptor implements HandlerInterceptor {

    // 进入controller层之前拦截请求
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestURI = request.getRequestURI();
//
//        // 排除特定的请求路径，比如 "/api/user/login"
//        if (loginExcludedPaths.stream().anyMatch(requestURI::contains)) {
//            return true; // 放行特定路径的请求
//        }

        // 在这里编写逻辑来检查登录凭证，例如从请求中获取token或者session进行验证
        // 如果验证失败，可以设置响应状态码或者重定向到登录页面;如果验证成功，返回true，允许请求继续处理
        // 方法一：使用token验证，从请求头中获取token进行验证
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            // 验证失败
            throw new BusinessException(401,"您没有权限访问，请先登录");
        }

        //检查并提取了有效的 JWT 令牌内容，去掉了 "Bearer " 前缀，以便后续的验证和处理
        String token = authorization.startsWith("Bearer ")
                ? authorization.replace("Bearer ", "")
                : authorization;

        BlogUser blogUser = JwtGenerator.parseToken(token);
        if (ObjectUtil.isNotEmpty(blogUser)) {
            request.setAttribute("user", blogUser);
            return true;
        }

        return false;
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
