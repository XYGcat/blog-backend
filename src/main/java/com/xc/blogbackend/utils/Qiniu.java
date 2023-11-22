package com.xc.blogbackend.utils;

import com.qiniu.util.Auth;

/**
 * 七牛云工具类
 *
 * @author 星尘
 */
public class Qiniu {
    /**
     * 生成验证token
     *
     * @param bucket
     * @param accessKey
     * @param secretKey
     * @return
     */
    public static String uploadToken(String bucket, String accessKey, String secretKey){
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(bucket);
        return token;
    }
}
