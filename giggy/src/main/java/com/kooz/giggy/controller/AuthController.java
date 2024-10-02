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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final GoogleOAuthClient googleOAuthClient;
    private final MemberService memberService;

    @Autowired
    private final JwtUtil jwtUtil;

    // handling AuthCode received by client
    @PostMapping("/google")
    public JwtResponse googleAuthLogin(@RequestBody GoogleOAuthRequest request) {
        GoogleOAuthResponse response = googleOAuthClient.getTokens(new GoogleOAuthRequest(request.getCode(), request.getEmail()));

        // TODO: 9/15/24 GoogleOAuthClient의 getUserInfo 통해서 id 받아온 이후 DB 저장 & jwt token 발급
        GoogleUserProfileResponse userProfile = googleOAuthClient.getUserProfile(new GoogleUserProfileRequest(response.accessToken()));

        String providerId = userProfile.getId();
        String email = userProfile.getEmail();;
        Optional<Member> loginMember = memberService.findByEmail(email);

        if (loginMember.isPresent()) {
            String accessToken = jwtUtil.generateAccessToken(providerId, UserRole.EMPLOYEE.getValue());
            String refreshToken = jwtUtil.generateRefreshToken(providerId);
            return new JwtResponse("Bearer", accessToken, refreshToken);
        } else {
            // 처음 로그인한 경우
            memberService.saveUser(OAuthProviderType.GOOGLE, providerId, email);
             
            String accessToken = jwtUtil.generateAccessToken(providerId, UserRole.EMPLOYEE.getValue());
            String refreshToken = jwtUtil.generateRefreshToken(providerId);
            return new JwtResponse("Bearer", accessToken, refreshToken);
        }
    }
}
