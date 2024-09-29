package com.kooz.giggy.service;

import com.kooz.giggy.dto.sign.SignUpRequest;
import com.kooz.giggy.dto.sign.SignUpResponse;
import com.kooz.giggy.entity.user.OAuthProviderType;
import com.kooz.giggy.entity.user.Member;
import com.kooz.giggy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
//    private final PasswordEncoder encoder;

    public Optional<Member> findByGoogleProviderId(String providerId) {
        return memberRepository.findByProviderId(providerId);
    }

    public Member saveUser(OAuthProviderType provider, String providerId) {
        Member user = Member.builder()
                .provider(provider)
                .providerId(providerId)
                .build();

        return memberRepository.save(user);
    }

    @Transactional
    public SignUpResponse registerMember(SignUpRequest request) {
        Member member = memberRepository.save(Member.from(request, new BCryptPasswordEncoder()));

        try {
            memberRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 사용중인 ID입니다.");
        }
        return SignUpResponse.from(member);
    }
}
