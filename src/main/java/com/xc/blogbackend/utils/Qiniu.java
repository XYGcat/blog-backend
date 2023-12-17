package com.xc.blogbackend.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.xc.blogbackend.config.QiniuConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
     * 删除七牛云中的图片(传入字符串)
     *
     * @param key
     * @return
     */
    public Boolean deleteFile(String key){
        String accessKey = qiniuConfig.getAccessKey();
        String secretKey = qiniuConfig.getSecretKey();
        String bucket = qiniuConfig.getBucketName();
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response delete = bucketManager.delete(bucket, key);
            if (delete.statusCode == 200 ) {
                return true;
            }
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println("Failed to delete file with key: " + key);
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
        return false;
    }

    /**
     * 删除七牛云中的图片(传入字符串数组)
     *
     * @param keys
     * @return
     */
    public Boolean deleteFile(List<String> keys) {
        String accessKey = qiniuConfig.getAccessKey();
        String secretKey = qiniuConfig.getSecretKey();
        String bucket = qiniuConfig.getBucketName();
        Configuration cfg = new Configuration(Region.region0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);

        boolean allDeleted = true; // 标记所有文件是否都删除成功

        for (String key : keys) {
            try {
//                //图片链接转码（中文路径可能会出错）
//                String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
                Response delete = bucketManager.delete(bucket, key);
                if (delete.statusCode != 200) {
                    allDeleted = false; // 如果有任何一个文件删除失败，将标记置为 false
                }
            } catch (QiniuException ex) {
                System.err.println("Failed to delete file with key: " + key);
                System.err.println(ex.code());
                System.err.println(ex.response.toString());
                allDeleted = false; // 如果遇到异常，也将标记置为 false
            }
        }
        return allDeleted; // 返回是否所有文件都删除成功的标记
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
//        String publicUrl = String.format("%s", imgUrl);   //String.format 方法用于将一个格式化字符串中的占位符替换为相应的值
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

    /**
     * 重命名文件
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    public Boolean renameImgKey(String oldKey,String newKey){
        String accessKey = qiniuConfig.getAccessKey();
        String secretKey = qiniuConfig.getSecretKey();
        String bucket = qiniuConfig.getBucketName();
        Configuration cfg = new Configuration(Region.region0());
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);

        try {
            // 发起重命名操作
            Response rename = bucketManager.rename(bucket, oldKey, newKey);
            if (rename.statusCode == 200 ) {
                return true;
            }
        } catch (QiniuException e) {
            System.err.println("文件重命名失败：" + e.getMessage());
            // 处理重命名失败的情况，例如输出错误信息或者进行其他处理
        }
        return false;
    }
}
