package com.xc.blogbackend.utils;

/**
 * 截取图片链接的工具类
 * 比如http://s4gm27ul6.hn-bkt.clouddn.com/article/bede7b3853e34b7d.png
 * 截取article/bede7b3853e34b7d.png
 *
 * @author 星尘
 */
public class StringManipulation {
    public static String subString(String imgUrl){
        // 根据斜杠进行分割
        String[] parts = imgUrl.split("/");

        // 如果分割后的部分数小于2，返回原始字符串
        if (parts.length < 2) {
            return imgUrl;
        }

        // 取倒数第二个和最后一个部分
        String secondLastPart = parts[parts.length - 2];
        String lastPart = parts[parts.length - 1];

        // 返回倒数第二个斜杠后的字符串
        return secondLastPart + "/" + lastPart;
    }
}
