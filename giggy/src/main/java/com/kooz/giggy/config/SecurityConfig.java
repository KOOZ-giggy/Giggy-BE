package com.kooz.giggy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http //.securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )   // 세션 없이 jwt 기반으로 진행
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/auth/**").permitAll()    //auth는 누구나 가능하도록
                        .anyRequest().authenticated() // 그 외엔 모두 인증 필요
                );
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
}
