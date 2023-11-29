package com.xc.blogbackend.utils;

import java.util.UUID;

/**
 * 图片命名随机生成
 *
 * @author 星尘
 */
public class ImageNamingUtil {

    /**
     * 图片命名随机生成
     *
     * @return
     */
    public static String generateUniqueImageName() {
        String uuid = UUID.randomUUID().toString();
        String imageName = uuid.replaceAll("-", "").substring(0, 16);

        // 加入字符常量前缀
        return imageName;
    }
}
