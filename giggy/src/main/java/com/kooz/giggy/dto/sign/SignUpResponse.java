package com.kooz.giggy.dto.sign;

import com.kooz.giggy.dto.jwt.JwtResponse;
import com.kooz.giggy.entity.user.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponse {

    private String name;
    private String contact;
    private String dob;
    private String email;

    public static SignUpResponse from(Member member) {
        return new SignUpResponse(
                member.getName(),
                member.getContact(),
                member.getDob(),
                member.getEmail()
        );
    }
}
