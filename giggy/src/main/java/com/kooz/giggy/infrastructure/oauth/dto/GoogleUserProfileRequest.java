package com.kooz.giggy.infrastructure.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleUserProfileRequest {

    private final String accessToken;
}
