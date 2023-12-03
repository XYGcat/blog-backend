package com.xc.blogbackend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 截取字符串的工具类
 *
 * @author 星尘
 */
public class StringManipulation {

    /**
     * 截取图片链接
     * 比如http://s4gm27ul6.hn-bkt.clouddn.com/article/bede7b3853e34b7d.png
     * 截取article/bede7b3853e34b7d.png
     * @param imgUrl
     * @return
     */
    public static String subString(String imgUrl){
        // 找到第一个斜杠的索引
        int firstSlashIndex = imgUrl.indexOf('/');

        // 找到第二个斜杠的索引，从第一个斜杠的下一个位置开始搜索
        int secondSlashIndex = imgUrl.indexOf('/', firstSlashIndex + 1);

        // 找到第三个斜杠的索引，从第二个斜杠的下一个位置开始搜索
        int thirdSlashIndex = imgUrl.indexOf('/', secondSlashIndex + 1);

        // 找到问号的索引，从第三个斜杠之后开始搜索
        int questionMarkIndex = imgUrl.indexOf('?', thirdSlashIndex + 1);

        // 如果存在第三个斜杠并且存在问号，截取第三个斜杠后到问号之间的内容并返回
        if (thirdSlashIndex != -1 && questionMarkIndex != -1) {
            return imgUrl.substring(thirdSlashIndex + 1, questionMarkIndex);
        } else if (thirdSlashIndex != -1) {
            // 如果不存在问号，截取第三个斜杠后的内容并返回
            return imgUrl.substring(thirdSlashIndex + 1);
        } else {
            // 处理 URL 中少于三个斜杠的情况，或者不存在问号
            return "Invalid URL";
        }
    }

    /**
     * 从时间中截取年份
     *
     * @param date
     * @return
     */
    public static String getYearFromDate(Date date) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        return yearFormat.format(date); // 格式化为年份字符串
    }
}
