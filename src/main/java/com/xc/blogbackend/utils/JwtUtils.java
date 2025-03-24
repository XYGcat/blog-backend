package com.xc.blogbackend.utils;

import com.xc.blogbackend.enums.ErrorCode;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.vo.UserVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.xc.blogbackend.constant.UserConstant.JWT_SECRET_KEY;

/**
 * 生成并解析Token的工具类
 *
 * @author 星尘
 */
public class JwtUtils {

    // 过期时间，单位秒
    public static final int ACCESS_TOKEN_EXPIRE_TIME = 60 * 60;
    public static final int REFRESH_TOKEN_EXPIRE_TIME = 60 * 60 * 24;

    /**
     * 生成唯一的JWT ID
     *
     * @return
     */
    public static String getJwtId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成密钥方法
     *
     * @return
     */
    public static SecretKey generalKey() {
        // 本地的密码解码（Base64解码）
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET_KEY);
        // HMACSHA256加密算法构造密钥
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "HMACSHA256");
    }

    /**
     * 生成 通用Token
     *
     * @param userInfo      用户信息
     * @param expireTime    过期时间（秒）
     * @param includeUserInfo 是否在Payload中包含用户信息
     * @return
     */
    public static String generateToken(UserVo userInfo, int expireTime, boolean includeUserInfo) {
        // 设置Payload部分（声明）
        Claims claims = Jwts.claims();
        claims.setSubject(String.valueOf(userInfo.getId()));
        if (includeUserInfo) {
            claims.put("roles", userInfo.getRoles());
        }

        // 指定header那部分签名的时候使用的签名算法，jjwt已经将这部分内容封装好了，只有{"alg":"HS256"}
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 设置令牌的过期时间
        Instant expirationInstant = Instant.now().plusSeconds(expireTime);
        Date expirationDate = Date.from(expirationInstant);
        // 生成签名的密钥
        SecretKey key = generalKey();

        // 生成JWT令牌
        return Jwts.builder()
                .setClaims(claims)                  // 设置载荷信息
                .setId(getJwtId())                  // JWT唯一身份标识
                .setExpiration(expirationDate)      // 设置过期时间
                .signWith(key,signatureAlgorithm)   // 设置签名及算法
                .compact();
    }

    /**
     * 生成 访问Token
     *
     * @param userInfo 用户信息
     * @return
     */
    public static String generateAccessToken(UserVo userInfo) {
        return generateToken(userInfo, ACCESS_TOKEN_EXPIRE_TIME, true);
    }

    /**
     * 生成 刷新Token
     *
     * @param userInfo 用户信息
     * @return
     */
    public static String generateRefreshToken(UserVo userInfo) {
        return generateToken(userInfo, REFRESH_TOKEN_EXPIRE_TIME, false);
    }

    /**
     * 解析Token
     *
     * @param token
     * @return
     */
    public static Claims parseToken(String token) {
        try {
            if (ObjectUtils.isEmpty(token)) {
                throw new BusinessException(ErrorCode.NULL_ERROR, "Token 不能为空");
            }
            // 签名秘钥
            SecretKey key = generalKey();
            // 解析JWT并获取Claims部分
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)     // 设置签名的秘钥
                    .build()
                    .parseClaimsJws(token); // 解析jwt

            // 返回载荷信息
            return claimsJws.getBody();
        }catch (Exception e){
            throw new BusinessException(ErrorCode.ACCESS_TOKEN_EXPIRE, "访问token已过期");
        }
    }

    /**
     * 判断Token是否过期
     *
     * @param claims
     * @return
     */
    public static Boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 使用反射将 Map 中的值填充到目标对象中
     *
     * @param target 目标对象
     * @param data   数据源（Map）
     * @param <T>    目标对象类型
     */
    public static <T> void populateFromMap(T target, Map<String, Object> data) {
        if (target == null || data == null) {
            return;
        }

        // 获取目标对象的所有字段
        Field[] fields = target.getClass().getDeclaredFields();

        for (Field field : fields) {
            // 确保访问私有字段
            field.setAccessible(true);
            if (data.containsKey(field.getName())) {
                try {
                    // 给目标对象的字段赋值
                    Object value = data.get(field.getName());
                    field.set(target, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to set field value: " + field.getName(), e);
                }
            }
        }
    }
}
