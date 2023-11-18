package com.xc.blogbackend.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class SecretKeyUtil {

    // 声明为类的静态常量
    public static final byte[] SECRET_KEY_BYTES = generateKeyBytes();

    // 生成HMAC-SHA-256算法的密钥的字节数组
    private static byte[] generateKeyBytes() {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return secretKey.getEncoded();
    }

    // 将字节数组转换为Base64字符串
    public static String keyBytesToBase64() {
        return Base64.getEncoder().encodeToString(SECRET_KEY_BYTES);
    }
}
