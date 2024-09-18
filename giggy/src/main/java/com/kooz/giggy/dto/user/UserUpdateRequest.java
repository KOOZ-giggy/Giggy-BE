package com.kooz.giggy.domain.user.dto;

import com.kooz.giggy.domain.user.entity.BizType;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class UserUpdateRequest {

    private Long id;
    private String password;
    private String contact;
    private String address;
    private String postCode;
    private BizType bizType;

    public void from(UserUpdateRequest request, PasswordEncoder encoder) {
        this.password = request.password == null || request.password.isBlank() ? this.password : encoder.encode(request.password);
        this.contact = request.contact;
        this.address = request.address;
        this.postCode = request.postCode;
        this.bizType = request.bizType;
    };
}
