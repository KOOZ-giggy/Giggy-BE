package com.kooz.giggy.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
// Client에게 전달할 TokenResponse
public class JwtResponse {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
