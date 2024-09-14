package com.kooz.giggy.infrastructure.oauth.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GoogleOAuthRequest {

    private final String code;
}
