package com.kooz.giggy.service;

import com.kooz.giggy.dto.sign.SignInResponse;
import com.kooz.giggy.dto.sign.SignUpRequest;
import com.kooz.giggy.dto.sign.SignUpResponse;
import com.kooz.giggy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public SignUpResponse registermember(SignUpRequest request) {

    }

}
