package com.kooz.giggy.infrastructure.oauth.client;

import com.kooz.giggy.infrastructure.oauth.dto.GoogleOAuthRequest;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleOAuthResponse;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleUserProfileRequest;
import com.kooz.giggy.infrastructure.oauth.dto.GoogleUserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

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
        String baseUrl = "https://oauth2.googleapis.com";
        WebClient webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("code", request.getCode());
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("redirect_uri", redirectUri);
        requestBody.put("grant_type", "authorization_code");

        GoogleOAuthResponse response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("code", request.getCode())
                        .path("/token")
                        .build()
                )
                .header(HttpHeaders.ACCEPT, "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GoogleOAuthResponse.class)
                .block();

        assert response != null;
        log.info("\n🟢response.body: {}", response.toString());

        getUserProfile(new GoogleUserProfileRequest(response.accessToken()));

        return response;
    }

    public GoogleUserProfileResponse getUserProfile(GoogleUserProfileRequest request) {
        log.info("\n🟢Request: {}", request);
        String baseUrl = "https://www.googleapis.com";
        WebClient webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .build();

        GoogleUserProfileResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/userinfo/v2/me")
                        .queryParam("access_token", request.getAccessToken())
                        .build())
                .retrieve()
                .bodyToMono(GoogleUserProfileResponse.class)
                .block();

        log.info("\n🟢Response: {}", response);

        return response;
    }
}
