package com.xc.blogbackend.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class QiniuTest {

    @Test
    void deleteFile() throws UnsupportedEncodingException {
        String key = "photo/生活/6dc6bc8d2b5543d7.webp";
        String accessKey = "";
        String secretKey = "";
        String bucket = "blog-xc";
//        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response delete = bucketManager.delete(bucket, key);
            if (delete.statusCode == 200 ) {
                System.out.println(key);
//                System.out.println(encodedKey);
                System.out.println("成功");
            }
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
//            System.out.println(encodedKey);
            System.err.println("Failed to delete file with key: " + key);
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}