package com.kooz.giggy.infrastructure.oauth.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class GoogleUserInfoRequest {

    private final String accessToken;
}
