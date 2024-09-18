package com.kooz.giggy.util;

import com.kooz.giggy.entity.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final String secretKey;
    private final String issuer;

    public JwtUtil(
            @Value("@{spring.jwt.secret}") String secretKey,
            @Value("@{spring.jwt.issuer}") String issuer
            ) {
        this.secretKey = secretKey;
        this.issuer = issuer;
    }

    public String generateToken(String userId, UserRole role) {
        Claims claims = Jwts.claims();

        claims.put("userId", userId);
        claims.put("userRole", role.getValue());

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setClaims(claims)// 클레임 설정
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS))) // 토큰 유효기간은 1시간
                .compact();
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", String.class);
    }

    public String getRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userRole", String.class);
    }

    public Boolean isExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().before(new Date());
    }
}
