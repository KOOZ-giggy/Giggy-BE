package com.kooz.giggy.dto.sign;

import com.kooz.giggy.entity.user.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    public String name;
    public String contact;
    public String dob;
    public String email;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getName(),
                member.getContact(),
                member.getDob(),
                member.getEmail()
        );
    }
}
