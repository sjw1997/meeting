package com.example.meeting.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.hmacShaKeyFor("sdkjfhkjewrhwkjhsdfhkjhsakfjhkjshkejrhkjwhrkjehwkjhfkjshdkjfhjklehrkjlwarewsdsdfsdferwrewsfsfsafderw".getBytes());
    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60; // 24小时

    // 从token中获取用户名
    public Integer getUserIdFromToken(String token) {
        return Integer.parseInt(getClaimFromToken(token, Claims::getSubject));
    }

    // 从token中获取过期时间
    public Date getExpirationDateFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getExpiration);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            return null;
        }
    }

    // 解析token中的所有声明
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    // 检查token是否过期
    private Boolean isTokenExpired(String token) {
        try {
            return getExpirationDateFromToken(token).before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 生成token
    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userId);
    }

    // 创建token
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(secretKey)
                .compact();
    }

    // 验证token
    public Boolean validateToken(String token) {
        return !isTokenExpired(token) && getAllClaimsFromToken(token) != null;
    }
}