package com.xc.blogbackend.config;

import com.xc.blogbackend.handler.AuthenticationInterceptor;
import com.xc.blogbackend.handler.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.xc.blogbackend.contant.UserConstant.loginInterceptPaths;
import static com.xc.blogbackend.contant.UserConstant.statusInterceptPaths;

/**
 * springboot注册拦截器
 *
 * @author 星尘
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns(loginInterceptPaths); // 设置需要拦截的路径

        // 权限拦截器
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns(statusInterceptPaths); // 设置需要拦截的路径
    }
}
