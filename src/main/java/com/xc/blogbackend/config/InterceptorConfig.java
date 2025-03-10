package com.xc.blogbackend.config;

import com.xc.blogbackend.constant.UserConstant;
import com.xc.blogbackend.handler.LoginCheckInterceptor;
import com.xc.blogbackend.handler.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
                .addPathPatterns(UserConstant.loginInterceptPaths);

        // 权限拦截器
        registry.addInterceptor(new PermissionInterceptor())
                .addPathPatterns("/**");
    }
}
