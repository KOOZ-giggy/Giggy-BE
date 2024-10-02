package com.kooz.giggy.dto.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleOAuthRequest {

    private String code;
//    private String email;

    @JsonCreator
    public GoogleOAuthRequest(
            @JsonProperty("code") String code
//            @JsonProperty("email") String email
    ) {
        this.code = code;
//        this.email = email;
    }
}
