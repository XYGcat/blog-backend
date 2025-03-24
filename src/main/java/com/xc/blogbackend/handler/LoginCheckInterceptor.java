package com.xc.blogbackend.handler;

import com.xc.blogbackend.context.UserContext;
import com.xc.blogbackend.enums.ErrorCode;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.vo.UserVo;
import com.xc.blogbackend.utils.JwtUtils;
import io.jsonwebtoken.Claims;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 在这里编写逻辑来检查登录凭证，例如从请求中获取token或者session进行验证
        // 如果验证失败，可以设置响应状态码或者重定向到登录页面;如果验证成功，返回true，允许请求继续处理
        // 方法一：使用token验证，从请求头中获取token进行验证
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN,"您没有权限访问，请先登录");
        }

        //检查并提取有效的 JWT 令牌内容
        String token = authorization.startsWith("Bearer ")
                ? authorization.replace("Bearer ", "")
                : authorization;

        Claims claims = JwtUtils.parseToken(token);
        Boolean tokenExpired = JwtUtils.isTokenExpired(claims);
        if (tokenExpired){
            throw new BusinessException(ErrorCode.ACCESS_TOKEN_EXPIRE, "访问token已过期");
        }

        // 使用反射将Claims中的值填充到UserVo对象中
        UserVo userVo = new UserVo();
        JwtUtils.populateFromMap(userVo, claims);
        // 将用户信息放入 ThreadLocal
        UserContext.setUser(userVo);

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
        // 清理 ThreadLocal，防止内存泄漏
        UserContext.clear();
    }
}
