package com.kooz.giggy.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;


@Component
@Slf4j
public class JwtUtil {

    private final Key secretKey;
    private final String issuer;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.issuer}") String issuer) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.issuer = issuer;
    }

    public String generateAccessToken(String userId, String role) {
        Claims claims = Jwts.claims();

        claims.put("userId", userId);
        claims.put("userRole", role);

        return Jwts.builder()
//                .signWith(SignatureAlgorithm.HS256, secretKey)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setClaims(claims)// 클레임 설정
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS))) // 토큰 유효기간은 1시간
                .compact();
    }

    public String generateRefreshToken(String userId) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setClaims(claims)
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS))) // 7일의 유효기간
                .compact();
    }

    public String getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId", String.class);
    }

    public String getRole(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userRole", String.class);
    }

    public Boolean isExpired(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build();

            Jws<Claims> claims = parser.parseClaimsJws(token);
            return claims.getBody();

        } catch(ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
