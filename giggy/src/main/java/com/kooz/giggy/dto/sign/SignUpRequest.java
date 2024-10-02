package com.kooz.giggy.dto.sign;

import com.kooz.giggy.entity.user.BizType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpRequest {
    // GOOGLE, APPLE
    private String providerType;
    // OAuth Code
    private String authCode;
    private String firstName;
    private String lastname;
    private String dob;
    private String contact;
    private String email;
}
