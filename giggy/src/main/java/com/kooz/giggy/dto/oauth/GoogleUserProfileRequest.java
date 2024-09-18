package com.kooz.giggy.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleUserProfileRequest {

    private final String accessToken;
}
