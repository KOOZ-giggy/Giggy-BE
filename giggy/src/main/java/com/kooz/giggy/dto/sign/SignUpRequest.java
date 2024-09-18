package com.kooz.giggy.dto.sign;

import com.kooz.giggy.entity.user.BizType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpRequest {
    private String name;
    private String password;
    private String contact;
    private String address;
    private String postCode;
    private String email;
    private BizType bizType;
}
