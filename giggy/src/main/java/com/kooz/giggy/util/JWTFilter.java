package com.kooz.giggy.util;

import com.kooz.giggy.entity.user.Member;
import com.kooz.giggy.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtUtil jwtUtil;

    private final MemberService memberService;

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

        String userId = jwtUtil.getUserId(token);
        // 추출 정보로 Member 찾기.
        Member loginMember = memberService.findByGoogleProviderId(userId).get();

        // loginMember 정보로 UsernamePasswordAuthentcationToken 발급

        //v1
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginMember.getProviderId(), null, List.of(new SimpleGrantedAuthority(jwtUtil.getRole(token))));
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginMember.getProviderId(), null, jwtUtil.getAuthentication(token).getAuthorities());
//        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        // v2, 정상 토큰의 경우 해당 토큰에서 Authenticationd을 추출하여 SecurityContext에 저장
        Authentication authentication = jwtUtil.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
