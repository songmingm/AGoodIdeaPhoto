package com.agoodidea.photoAlbum.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * jwt工具类
 */
@Component
@Slf4j
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    /**
     * 设置token过期时间2小时
     */
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 2;

    /**
     * 生成token私钥
     */
    private static final String JWT_KEY = UUID.randomUUID().toString();

    /**
     * @param json 根据登录用户信息生成token
     */
    public static String buildJwt(String json) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(json)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, JWT_KEY);
        String p = builder.compact();
        return builder.compact();
    }

    /**
     * 解析JWT信息
     */
    public static Claims parseJwt(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(JWT_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.info("JWT验证失败:{}", token);
        }
        return claims;
    }
}
