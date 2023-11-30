package com.xc.blogbackend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringManipulationTest {

    @Test
    void subString() {
        String url1 = "http://s4gm27ul6.hn-bkt.clouddn.com/article/bede7b3853e34b7d.png";
        String url2 = "http://s4gm27ul6.hn-bkt.clouddn.com/photo/%E7%94%9F%E6%B4%BB/87dad9d445f047f7.webp";

        System.out.println("Extracted content from URL 1: " + StringManipulation.subString(url1));
        System.out.println("Extracted content from URL 2: " + StringManipulation.subString(url2));
    }
}