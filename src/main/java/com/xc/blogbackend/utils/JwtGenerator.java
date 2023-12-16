package com.xc.blogbackend.utils;

import com.xc.blogbackend.model.domain.BlogUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.xc.blogbackend.contant.BlogUserConstant.JWT_SECRET;

/**
 * 生成并解析Token的工具类
 *
 * @author 星尘
 */
public class JwtGenerator {

    // jti：jwt的唯一身份标识
    public static final String JWT_ID = UUID.randomUUID().toString();

    // 过期时间，单位毫秒
    public static final int EXPIRE_TIME = 60 * 60 * 1000; // 一个小时

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HMACSHA256");
        return key;
    }

    /**
     * 生成 Token
     *
     * @param blogUser
     * @return
     */
    public static String generateToken(BlogUser blogUser) {
        // 设置头部信息
//		Map<String, Object> header = new HashMap<String, Object>();
//		header.put("typ", "JWT");
//		header.put("alg", "HS256");

        // 指定header那部分签名的时候使用的签名算法，jjwt已经将这部分内容封装好了，只有{"alg":"HS256"}
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证的方式）
        Claims claims = Jwts.claims();
        claims.setSubject(blogUser.getUsername());  //jwt所面向的用户，放登录的用户名，一个json格式的字符串，可存放userid，roldid之类，作为用户的唯一标志
        claims.put("nick_name", blogUser.getNick_name());
        claims.put("id", blogUser.getId());
        claims.put("role", blogUser.getRole());

        // 设置令牌的过期时间
        Instant expirationInstant = Instant.now().plusSeconds(EXPIRE_TIME);
        Date expirationDate = Date.from(expirationInstant);

        // 生成签名的时候使用的秘钥secret
        SecretKey key = generalKey();


        // 生成令牌,为payload添加各种标准声明和私有声明
        return Jwts.builder()
    //			.setHeader(header) // 设置头部信息
                .setClaims(claims)  // 如果有私有声明，一定要先设置自己创建的这个私有声明，这是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明
                .setId(JWT_ID) //jwt的唯一身份标识，根据业务需要，可以设置为一个不重复的值，主要用来作为一次性token，从而回避重放攻击
                .setExpiration(expirationDate)  // 设置过期时间
                .signWith(key,signatureAlgorithm)   // 设置签名，使用的是签名算法和签名使用的秘钥
                .compact();
    }

    /**
     * 解析Token
     *
     * @param token
     * @return
     */
    public static BlogUser parseToken(String token) {
        try {
            if (token == null) {
                throw new IllegalArgumentException("Token 不能为空");
                // 或者返回默认的用户信息或其他处理方式
            }

            SecretKey key = generalKey(); // 签名秘钥，和生成的签名的秘钥一模一样

            // 解析令牌
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key) // 设置签名的秘钥
                    .build()
                    .parseClaimsJws(token); // 设置需要解析的jwt

            // 获取载荷信息
            Claims body = claimsJws.getBody();

            // 获取令牌过期时间
            Date expiration = body.getExpiration();

            // 验证过期时间
            if (expiration != null && expiration.before(new Date())) {
                // Token 已过期
                throw new RuntimeException("Token 已过期");
            }

            // 返回用户信息对象
            BlogUser blogUser = new BlogUser();
            blogUser.setUsername(body.getSubject());
            blogUser.setNick_name(body.get("nick_name", String.class));
            blogUser.setId(body.get("id", Integer.class));
            blogUser.setRole(body.get("role", Integer.class));

            return blogUser;
        }catch (Exception e){
            // 处理解析异常或者验证失败
            throw new RuntimeException("无效的 Token 或签名错误", e);
        }
    }
}
