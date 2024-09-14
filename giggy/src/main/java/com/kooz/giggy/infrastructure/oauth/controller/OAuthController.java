package com.kooz.giggy.infrastructure.oauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kooz.giggy.infrastructure.oauth.client.GoogleOAuthClient;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleOAuthRequest;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleOAuthResponse;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleUserInfoRequest;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleOAuthClient googleOAuthClient;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    // handling AuthCode received by client
    @PostMapping("/google")
    public void googleAuthLogin(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");

        GoogleOAuthRequest request = new GoogleOAuthRequest(code);
        GoogleOAuthResponse response = googleOAuthClient.getTokens(request);
        String accessToken = response.accessToken();
        String refreshToken = response.refreshToken();

        System.out.println(response);



        // TODO: 9/15/24 GoogleOAuthClient의 getUserInfo 통해서 id 받아온 이후 DB 저장 & jwt token 발급
        String userId = googleOAuthClient.getUserInfo(new GoogleUserInfoRequest(accessToken)).getId();
//        String userAuthUri = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/userinfo/v2/me")
//                .queryParam("access_token", accessToken)
//                .toUriString();
//
//
//        ResponseEntity<Map> responseEntity = restTemplate.exchange(userAuthUri, HttpMethod.GET, null, Map.class);

//        log.info("🟢GET TOKENS: \n {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }


    @RequestMapping("/api/auth")
    public class AuthController {


        // GET POST  email
        // GET POST oauth/{type}

    }
}
