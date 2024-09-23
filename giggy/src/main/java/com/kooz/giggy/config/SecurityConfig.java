package com.kooz.giggy.config;

import com.kooz.giggy.util.JWTFilter;
import com.kooz.giggy.util.JwtUtil;
import com.kooz.giggy.util.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration configuration;
    private final JwtUtil jwtUtil;

    private final String[] allowsUrls = {"/", "/swagger-ui/**", "/api/v1/auth", "/api/v1/sign-in", "/api/v1/sign-up"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http //.securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )   // 세션 없이 jwt 기반으로 진행
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(allowsUrls).permitAll() // allowsUrls의 경우 permit
                        .anyRequest().authenticated() // 그 외엔 모두 인증 필요
                )
                .sessionManagement(sessionManagement -> sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 기존 UsernamePWAuthentcationFilter를 로그인 필터로 대체
                .addFilterAt(new LoginFilter(configuration.getAuthenticationManager(), jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

//                .oauth2Login(oauth2 -> oauth2
//                                .successHandler(oauth2SuccessHandler)
//                                .userInfoEndpoint(Customizer.withDefaults())
//                                .service
//                        .successHandler()
                        //TODO: successHandler, userService(customOAuth2UserService) 구현 필요함.

//                );
//                .userDetailsService()




        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
