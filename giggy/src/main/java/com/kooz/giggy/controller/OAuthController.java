package com.kooz.giggy.controller;

import com.kooz.giggy.dto.jwt.JwtResponse;
import com.kooz.giggy.dto.oauth.GoogleOAuthRequest;
import com.kooz.giggy.dto.oauth.GoogleOAuthResponse;
import com.kooz.giggy.dto.oauth.GoogleUserProfileRequest;
import com.kooz.giggy.dto.oauth.GoogleUserProfileResponse;
import com.kooz.giggy.entity.user.Member;
import com.kooz.giggy.entity.user.OAuthProviderType;
import com.kooz.giggy.entity.user.UserRole;
import com.kooz.giggy.service.MemberService;
import com.kooz.giggy.service.GoogleOAuthClient;
import com.kooz.giggy.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleOAuthClient googleOAuthClient;
    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    // handling AuthCode received by client
    @PostMapping("/google")
    public JwtResponse googleAuthLogin(@RequestBody GoogleOAuthRequest request) {
        GoogleOAuthResponse response = googleOAuthClient.getTokens(new GoogleOAuthRequest(request.getCode()));
        // TODO: 9/15/24 GoogleOAuthClient의 getUserInfo 통해서 id 받아온 이후 DB 저장 & jwt token 발급
        GoogleUserProfileResponse userProfile = googleOAuthClient.getUserProfile(new GoogleUserProfileRequest(response.accessToken()));

        String providerId = userProfile.getId();
        Optional<Member> loginMember = memberService.findByGoogleProviderId(providerId);

        // TODO: 9/15/24 받아온 UserID를 DB에 저장 이후, jwt token 발급
//        if (memberService.findByGoogleProviderId(providerId).isEmpty()) {
//            // 미가입 계정의 경우
//            memberService.saveUser(OAuthProviderType.GOOGLE, providerId);
//            String token = jwtUtil.generateToken(providerId, UserRole.EMPLOYEE.getValue());
//
//        } else {
//            // 기가입 계정의 경우
////            String token = jwtTokenProvider
//        }

        if (loginMember.isPresent()) {
            String token = jwtUtil.generateToken(providerId, UserRole.EMPLOYEE.getValue());
            return new JwtResponse("Bearer", token);
        } else {
            // 처음 로그인한 경우
            memberService.saveUser(OAuthProviderType.GOOGLE, providerId);
            String token = jwtUtil.generateToken(providerId, UserRole.EMPLOYEE.getValue());
            return new JwtResponse("Bearer", token);
        }
    }
}
