package com.xc.blogbackend.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.util.Auth;
import com.xc.blogbackend.config.QiniuConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 七牛云工具类
 *
 * @author 星尘
 */
@Component
public class Qiniu {

    @Resource
    private QiniuConfig qiniuConfig;

    /**
     * 生成验证token
     *
     * @return
     */
    public String uploadToken(){
        String accessKey = qiniuConfig.getAccessKey();
        String secretKey = qiniuConfig.getSecretKey();
        String bucket = qiniuConfig.getBucketName();
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(bucket);
        return token;
    }

    /**
     * 生成下载图片的token
     *
     * @return
     */
    public String downloadUrl(String imgUrl) throws QiniuException {
        //手动拼接
        String secretKey = qiniuConfig.getSecretKey();
        String accessKey = qiniuConfig.getAccessKey();
//        String publicUrl = String.format("%s", imgUrl);
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;   //自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(imgUrl, expireInSeconds);

          //SDK自动拼接
//        String domain = qiniuConfig.getDomain();    // domain   下载 domain, eg: qiniu.com【必须】
//        boolean useHttps = true;                    // useHttps 是否使用 https【必须】
//        // key      下载资源在七牛云存储的 key【必须】
//        DownloadUrl url = new DownloadUrl(domain, useHttps, key);
////        url.setAttname(attname)                             // 设置下载链接的附件名（attname）
////           .setFop(fop)                                     // 配置数据处理操作（fop）
////           .setStyle(style, styleSeparator, styleParam);    // 配置样式处理（style）
//        // 带有效期
//        long expireInSeconds = 3600;                        //1小时，可以自定义链接过期时间
//        long deadline = System.currentTimeMillis()/1000 + expireInSeconds;
//        Auth auth = Auth.create(accessKey, secretKey);
//        String urlString = url.buildURL(auth, deadline);

        return finalUrl;
    }
}
