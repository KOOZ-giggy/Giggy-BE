package com.kooz.giggy.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Authorization check
        String authorization = request.getHeader("Authorization");

        // Validate Authorization
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("Invalid header request: " + request.toString());
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            log.info("Expired Token request" + token);
            filterChain.doFilter(request, response);
            return;
        }

        String longId = jwtUtil.getUserId(token);

    }
}
