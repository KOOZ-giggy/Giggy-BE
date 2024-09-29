package com.kooz.giggy.util;

import com.kooz.giggy.entity.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    public String generateToken(String userId, String role) {
        Claims claims = Jwts.claims();

        claims.put("userId", userId);
        claims.put("userRole", role);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setClaims(claims)// 클레임 설정
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // 토큰 발급 시간
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS))) // 토큰 유효기간은 1시간
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
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch(ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
