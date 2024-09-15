package com.kooz.giggy.infrastructure.oauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kooz.giggy.infrastructure.oauth.client.GoogleOAuthClient;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleOAuthRequest;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleOAuthResponse;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleUserProfileRequest;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleUserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleOAuthClient googleOAuthClient;

    // handling AuthCode received by client
    @PostMapping("/google")
    public void googleAuthLogin(@RequestBody GoogleOAuthRequest request) {
        GoogleOAuthResponse response = googleOAuthClient.getTokens(new GoogleOAuthRequest(request.getCode()));
        // TODO: 9/15/24 GoogleOAuthClient의 getUserInfo 통해서 id 받아온 이후 DB 저장 & jwt token 발급
        GoogleUserProfileResponse userProfile = googleOAuthClient.getUserProfile(new GoogleUserProfileRequest(response.accessToken()));

        // TODO: 9/15/24 받아온 UserID를 DB에 저장 이후, jwt token 발급
    }
}
