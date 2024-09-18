package com.kooz.giggy.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private final String secretKey;
    private final String issuer;

    public JwtTokenProvider(
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.issuer}") String issuer
    ) {
        this.secretKey = secretKey;
        this.issuer = issuer;
//        this.key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8))
    }

    public String generateToken(String userId) {
        Claims claims = Jwts.claims()
                .setSubject(userId);
        
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

        return claims.getSubject();
    }

    public Boolean isExpired(String token) {
        Claims claims Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().before(new Date());
    }
}

//public class JwtTokenProvider {
//
//    @Value("${spring.jwt.secret}")
//    private String SECRET_KEY;
//    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
//
//    // Create Token
//    public String generateToken(String userId) {
//     return Jwts.builder()
//             .setSubject(userId)
//             .setIssuedAt(new Date())
//             .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//             .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//             .compact();
//    }
//
//    public String getUserIdFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//}
