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

//    public GoogleOAuthRequest(String code) {
//        this.code = code;
//    }
    @JsonCreator
    public GoogleOAuthRequest(@JsonProperty("code") String code) {
        this.code = code;
    }
}
