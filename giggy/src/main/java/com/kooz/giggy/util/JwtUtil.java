package com.kooz.giggy.util;

import com.kooz.giggy.dto.jwt.JwtResponse;
import com.kooz.giggy.service.CustomUserDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Slf4j
public class JwtUtil {

    private final Key secretKey;
    private final String issuer;
    private static final String USER_ROLE_KEY = "userRole";

    @Autowired
    private CustomUserDetailService customUserDetailService;

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
        claims.put(USER_ROLE_KEY, role);

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

    public void getAuthority(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String userId = (String) claims.get("userId");

        // Claim에서 권한 정보 가져오기.
        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(USER_ROLE_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = new User(userId, "", authorities);
//        UserDetails userdetails = customUserDetailService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
//        return new UsernamePasswordAuthenticationToken(userdetails, "", authorities);
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

    public JwtResponse generateJwtResponse(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = generateAccessToken(authentication.getName(), authorities);
        String refreshToken = generateRefreshToken(authentication.getName());

        return new JwtResponse("Bearer", accessToken, refreshToken);
    }
}
