package com.kooz.giggy.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOAuthResponse(
        // googleapis 호출을 위한 token
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("scope") String scope,
        @JsonProperty("token_type") String tokenType
) {
}