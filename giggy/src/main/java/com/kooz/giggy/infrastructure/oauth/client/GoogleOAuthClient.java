package com.kooz.giggy.infrastructure.oauth.client;

import com.kooz.giggy.infrastructure.oauth.dto.GoogleOAuthRequest;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleOAuthResponse;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleUserInfoRequest;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthClient {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    // AuthCode 2 Google Token
    public GoogleOAuthResponse getTokens(GoogleOAuthRequest request) {
        String endpoint = "https://oauth2.googleapis.com/token";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("code", request.getCode())
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("grant_type", "authorization_code")
                .toUriString();

        log.info("🟢 requestURL: " + requestUrl);

        ResponseEntity<GoogleOAuthResponse> response = restTemplate.exchange(
                requestUrl, HttpMethod.POST, null, GoogleOAuthResponse.class);
        log.info("🟢 response.body:" + response.toString());
        return response.getBody();
    }

    public GoogleUserInfoResponse getUserInfo(GoogleUserInfoRequest request) {
        String endpoint = "https://www.googelapis.com/userinfo/v2/me";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("access_token", request.getAccessToken())
                .toUriString();

        ResponseEntity<GoogleUserInfoResponse> response = restTemplate.exchange(
                requestUrl, HttpMethod.GET, null, GoogleUserInfoResponse.class
        );

        return response.getBody();
    }
}
