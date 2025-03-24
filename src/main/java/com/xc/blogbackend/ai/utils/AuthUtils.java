package com.xc.blogbackend.ai.utils;

import okhttp3.HttpUrl;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;

/**
 * 鉴权工具类
 */
public class AuthUtils {
    /**
     * 日期格式化
     */
    public final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    // 鉴权字符串模板，包含host、date、GET请求行
    public final static String preStr = "host: %s\n" +
            "date: %s\n" +
            "GET %s HTTP/1.1";

    /**
     * 鉴权方法
     *
     * @param hostUrl   地址
     * @param apiKey    apikey
     * @param apiSecret apiSecret
     * @return 鉴权信息
     */
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret)
            throws MalformedURLException, InvalidKeyException, NoSuchAlgorithmException {
        // 根据地址创建URL对象
        URL url = new URL(hostUrl);
        // 获取当前时间
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        String date = now.format(dateTimeFormatter); // 格式化日期时间。

        // 获取Mac实例，指定算法为"hmacsha256"
        Mac mac = Mac.getInstance("hmacsha256");
        // 创建密钥规范
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        // 使用密钥初始化Mac对象
        mac.init(spec);
        // 计算消息摘要
        byte[] hexDigits = mac.doFinal(String.format(preStr, url.getHost(), date, url.getPath()).getBytes(StandardCharsets.UTF_8));

        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);

        // 拼接授权信息
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);

        // 构建URL
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build(); // 构建HTTP URL。

        // 返回HTTP URL的字符串形式
        return httpUrl.toString();
    }
}
