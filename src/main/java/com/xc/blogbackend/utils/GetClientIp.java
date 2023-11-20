package com.xc.blogbackend.utils;

import javax.servlet.http.HttpServletRequest;

/**
 *获取用户ip
 *
 * @author 星尘
 */
public class GetClientIp {
    public static String getClientIp(HttpServletRequest request) {
        // 尝试从请求头中获取X-Real-IP，该请求头通常由一些代理服务器设置
        String ipAddress = request.getHeader("X-Real-IP");

        // 如果X-Real-IP不存在或为空或为"unknown"，尝试获取X-Forwarded-For请求头
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Forwarded-For");
        }

        // 如果X-Forwarded-For不存在或为空或为"unknown"，尝试获取Proxy-Client-IP请求头
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        // 如果Proxy-Client-IP不存在或为空或为"unknown"，尝试获取WL-Proxy-Client-IP请求头
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        // 如果WL-Proxy-Client-IP不存在或为空或为"unknown"，尝试获取RemoteAddr
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }
}
