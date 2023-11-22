package com.xc.blogbackend.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class IpUtilsTest {

    @Test
    void getCityInfo() throws IOException {
        String ip = null;
        // 深圳百度
        //ip = "14.215.177.39";
        // 上海阿里云
//        ip = "106.11.253.83";

        //ip = "110.53.80.9";

       String location = IpUtils.getLocation("110.53.80.9");
//        String location = IpUtils.getCityInfo("110.53.80.9");
        System.out.println(location);

    }
}