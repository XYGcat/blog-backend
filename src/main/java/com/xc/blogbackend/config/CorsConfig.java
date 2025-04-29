package com.xc.blogbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置(Spring MVC 的静态 CORS 映射)
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")                      // 允许所有接口路径
                        .allowedOrigins("http://localhost:8000",  "http://www.xcstardust.com")     // 允许的前端域名
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 支持的方法
                        .allowedHeaders("*")    // 支持的请求头
                        .allowCredentials(true) // 允许带 Cookie
                        .maxAge(3600);          // 预检请求缓存时间（单位：秒）
            }
        };
    }
}
