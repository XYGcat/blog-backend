package com.xc.blogbackend.utils;

/**
 * 获取当前type类型数字的公共类
 *
 * @author 星尘
 */
public class CurrentTypeName {
    public static String getCurrentTypeName(Integer type){
        String res = "";
        switch (type) {
            case 1:
                res = "文章";
                break;
            case 2:
                res = "说说";
                break;
            case 3:
                res = "留言";
                break;
            default:
                res = "未知类型";
                break;
        }
        return res;
    }
}
