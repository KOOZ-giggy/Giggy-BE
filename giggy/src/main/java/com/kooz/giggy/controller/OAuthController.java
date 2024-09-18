package com.kooz.giggy.infrastructure.oauth.controller;

import com.kooz.giggy.domain.user.entity.OAuthProviderType;
import com.kooz.giggy.domain.user.service.UserService;
import com.kooz.giggy.infrastructure.oauth.client.GoogleOAuthClient;
import com.kooz.giggy.infrastructure.oauth.dto.*;
import com.kooz.giggy.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleOAuthClient googleOAuthClient;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    // handling AuthCode received by client
    @PostMapping("/google")
    public void googleAuthLogin(@RequestBody GoogleOAuthRequest request) {
        GoogleOAuthResponse response = googleOAuthClient.getTokens(new GoogleOAuthRequest(request.getCode()));
        // TODO: 9/15/24 GoogleOAuthClient의 getUserInfo 통해서 id 받아온 이후 DB 저장 & jwt token 발급
        GoogleUserProfileResponse userProfile = googleOAuthClient.getUserProfile(new GoogleUserProfileRequest(response.accessToken()));

        String providerId = userProfile.getId();

        // TODO: 9/15/24 받아온 UserID를 DB에 저장 이후, jwt token 발급
        if (userService.findByGoogleProviderId(providerId).isEmpty()) {
            // 미가입 계정의 경우
            userService.saveUser(OAuthProviderType.GOOGLE, providerId);
        } else {
            // 기가입 계정의 경우
//            String token = jwtTokenProvider
        }
    }
}
