package com.kooz.giggy.dto.sign;

import com.kooz.giggy.entity.user.BizType;
import com.kooz.giggy.entity.user.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponse {

    private String name;
    private String contact;
    private String address;
    private String postCode;
    private String email;
    private BizType bizType;

    public static SignUpResponse from(Member member) {
        return new SignUpResponse(
                member.getName(),
                member.getContact(),
                member.getAddress(),
                member.getPostCode(),
                member.getEmail(),
                member.getBizType()
        );
    }
}
