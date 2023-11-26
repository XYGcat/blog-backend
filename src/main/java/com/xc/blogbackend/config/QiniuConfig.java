package com.xc.blogbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "qiniu")
@Data
public class QiniuConfig {
    //秘钥
    private String accessKey;
    //秘钥
    private String secretKey;
    //域名
    private String domain;
    //存储空间名
    private String bucketName;
    //本地 local 七牛云 qiniu  云服务器 online
    private String UPLOADTYPE;
    //服务器地址 用于拼接图片显示 可以使用七牛云测试域名 前面请带上http://或者https://根据实际情况带上
    private String BASEURL;

}
