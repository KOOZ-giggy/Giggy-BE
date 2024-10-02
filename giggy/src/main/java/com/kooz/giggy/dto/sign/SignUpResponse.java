package com.kooz.giggy.dto.sign;

import com.kooz.giggy.dto.jwt.JwtResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class SignUpResponse {

    private JwtResponse jwtResponse;
    private MemberResponse memberResponse;
}
